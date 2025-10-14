package com.dungeonescape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.CombatManager;
import controller.DoorManager;
import controller.ItemManager;
import controller.LevelManager;
import controller.TrapManager;
import controller.UIStateManager;
import exceptions.InvalidMoveException;
import model.Bean;
import model.Elfo;
import model.Enemy;
import model.EnemyFactory;
import model.Item;
import model.Level;
import model.Lucy;
import model.Player;
import model.ShopEncounter;
import model.TrapFactory;
import model.Weapon;
import utils.DiceRoller;
import view.ControlPanel;
import view.DungeonPanel;
import view.GameWindow;
import view.HUDPanel;
import view.InventoryPanel;
import view.LogPanel;
import view.PartyPanel;
import view.ShopDialog;

/**
 * The main controller for the game, coordinating between managers.
 */
public class Game {
    // Core models
    private Player activePlayer;
    private final List<Player> party;
    private Enemy currentEnemy;

    // Managers (single responsibility classes)
    private final DiceRoller dice;
    private final DoorManager doorManager;
    private final TrapManager trapManager;
    private final LevelManager levelManager;
    private final ItemManager itemManager;
    private final UIStateManager uiManager;
    private CombatManager combatManager;
    private final ShopEncounter shopEncounter;

    // Tracking
    private final Map<String, Integer> enemyEncounterCount;

    public Game(Player player, GameWindow gameWindow, PartyPanel partyPanel, DungeonPanel dungeonPanel, InventoryPanel inventoryPanel, LogPanel logPanel, ControlPanel controlPanel, HUDPanel hudPanel) {
        // Initialize player and party
        this.activePlayer = player;
        this.party = new ArrayList<>();
        initializeParty();
        // Initialize utilities
        this.dice = new DiceRoller();
        this.enemyEncounterCount = new HashMap<>();
        // Initialize managers
        EnemyFactory enemyFactory = new EnemyFactory(dice);
        TrapFactory trapFactory = new TrapFactory(dice);
        this.doorManager = new DoorManager(dice, enemyFactory, enemyEncounterCount);
        this.trapManager = new TrapManager(trapFactory, dice);
        this.levelManager = new LevelManager(dice);
        this.itemManager = new ItemManager();
        this.uiManager = new UIStateManager(dungeonPanel, controlPanel, inventoryPanel,
                                            hudPanel, partyPanel, logPanel);
        this.shopEncounter = new ShopEncounter();
        // Setup UI listeners
        setupListeners(dungeonPanel, controlPanel);
        // Start the game
        startGame();
    }

    private void initializeParty() {
        this.party.add(this.activePlayer);
        if (!(activePlayer instanceof Elfo)) party.add(new Elfo());
        if (!(activePlayer instanceof Bean)) party.add(new Bean());
        if (!(activePlayer instanceof Lucy)) party.add(new Lucy());
    }

    private void setupListeners(DungeonPanel dungeonPanel, ControlPanel controlPanel) {
        dungeonPanel.door1Button.addActionListener(e -> chooseDoor(1));
        dungeonPanel.door2Button.addActionListener(e -> chooseDoor(2));
        controlPanel.addAttackListener(e -> performCombatRound());
        controlPanel.addSpecialListener(e -> useSpecialAbility());
        controlPanel.addFleeListener(e -> fleeEncounter());
        controlPanel.addInventoryListener(e -> manageInventory());
        controlPanel.addContinueListener(e -> {
            uiManager.setDoorMode();
            updateGUI();
        });
        controlPanel.addShopListener(e -> openShop());
        controlPanel.addExitListener(e -> System.exit(0));
    }

    private void startGame() {
        uiManager.getLogPanel().addMessage("Welcome to Dungeon Escape!");
        equipStartingWeapon();
        startNextLevel();
    }

    private void equipStartingWeapon() {
        Item startingWeapon = activePlayer.getInventory().getItems().stream()
            .filter(item -> item instanceof Weapon)
            .findFirst()
            .orElse(null);
        if (startingWeapon != null) {
            try {
                ItemManager.ItemUseResult result = itemManager.useItem(activePlayer, startingWeapon.getName());
                uiManager.getLogPanel().addMessage(result.message());
            } catch (InvalidMoveException e) {
                uiManager.getLogPanel().addMessage("Error equipping starting weapon: " + e.getMessage());
            }
        }
    }

    private void startNextLevel() {
        Level<Enemy> level = levelManager.getCurrentLevel();
        uiManager.loadBackgroundImage(level.getBackgroundImagePath());
        uiManager.getLogPanel().addMessage("\n--- You have entered " + level.getName() + " ---");
        doorManager.setCurrentEncounterDeck(levelManager.getShuffledEncounterDeck());
        uiManager.setDoorMode();
        updateGUI();
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public LogPanel getLogPanel() {
        return uiManager.getLogPanel();
    }

    private void chooseDoor(int doorNumber) {
        chooseDoor(doorNumber, null);
    }

    private void chooseDoor(int doorNumber, String enemyToAvoid) {
        if (currentEnemy != null) return; // Can't open a door during combat

        String doorName = (doorNumber == 1) ? "left" : "right";
        uiManager.getLogPanel().addMessage("\nYou open the " + doorName + " door...");
        // Delegate encounter generation to DoorManager
        DoorManager.EncounterResult encounter = doorManager.generateEncounter(activePlayer, enemyToAvoid);
        switch (encounter.type()) {
            case ENEMY -> handleEnemyEncounter(encounter.enemy());
            case TRAP -> handleTrapEncounter();
            case EMPTY_ROOM -> handleEmptyRoom();
            case LEVEL_COMPLETE -> handleLevelComplete();
        }
    }

    private void handleEnemyEncounter(Enemy enemy) {
        if (enemy.getName().equals(currentEnemy != null ? currentEnemy.getName() : "")) {
            uiManager.getLogPanel().addMessage("...but you manage to dodge into a different corridor!");
        }
        enterCombat(enemy);
    }

    private void handleTrapEncounter() {
        // Delegate to TrapManager
        TrapManager.TrapResult result = trapManager.handleTrap(activePlayer);
        uiManager.getLogPanel().addMessage(result.getAllMessages());
        uiManager.displayImage(result.imagePath);
        updateGUI();
        if (result.playerDied) {
            endGame();
        } else {
            uiManager.setPostEncounterMode();
        }
    }

    private void handleEmptyRoom() {
        uiManager.displayImage("images/ui/EmptyRoom.png");
        uiManager.getLogPanel().addMessage("The room is empty... but you spot something glinting in the corner!");
        activePlayer.addGold(10);
        uiManager.getLogPanel().addMessage("You found 10 gold!");
        updateGUI();
        uiManager.setPostEncounterMode();
    }

    private void handleLevelComplete() {
        uiManager.getLogPanel().addMessage("You've cleared this section of the dungeon!");
        if (levelManager.advanceToNextLevel()) {
            startNextLevel();
        } else {
            winGame();
        }
    }

    private void openShop() {
        if (currentEnemy != null) return; // Can't open shop during combat
        uiManager.getLogPanel().addMessage(shopEncounter.getEnterMessage());

        // Open the shop UI
        ShopDialog shopDialog = new ShopDialog(this, shopEncounter);
        shopDialog.setVisible(true);
        // The game is modal, so it will pause here until the dialog is closed.
        uiManager.getLogPanel().addMessage("You leave the shop.");
        updateGUI(); // Refresh GUI in case gold or items changed
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        this.combatManager = new CombatManager(activePlayer, currentEnemy, dice); // Create a new combat manager
        String enemyName = enemy.getName();
        int level = enemyEncounterCount.getOrDefault(enemyName, 0) + 1;
        uiManager.getLogPanel().addMessage("\nA " + enemyName + " (Lvl " + level + ") appears!");
        uiManager.getLogPanel().addMessage(enemy.getHint());
        uiManager.setCombatMode(activePlayer);
        uiManager.displayImage(enemy.getImagePath());
        updateGUI();
    }

    private void performCombatRound() {
        if (combatManager == null || !activePlayer.isAlive()) {
            return;
        }
        // Delegate all combat logic to the CombatManager
        String combatLog = combatManager.performCombatRound();
        uiManager.getLogPanel().addMessage(combatLog);

        // After the round, check the results
        if (!activePlayer.isAlive()) {
            endGame();
        } else if (!currentEnemy.isAlive()) {
            winCombat();
        } else {
            uiManager.updateSpecialAbilityButton(activePlayer);
            updateGUI();
        }
    }

    private void useSpecialAbility() {
        if (combatManager == null || !activePlayer.isAlive()) {
            return;
        }

        // Delegate to the CombatManager
        String abilityResult = combatManager.useSpecialAbility();
        uiManager.getLogPanel().addMessage(abilityResult);

        // Check results after the ability and potential enemy counter-attack
        if (!activePlayer.isAlive()) {
            endGame();
            return;
        } else if (!currentEnemy.isAlive()) {
            winCombat();
            return;
        } else {
            uiManager.updateSpecialAbilityButton(activePlayer);
            updateGUI();
        }
    }

    private void winCombat() {
        String enemyName = currentEnemy.getName();
        uiManager.getLogPanel().addMessage("You defeated the " + enemyName + "!");

        // Update encounter count
        doorManager.incrementEnemyCount(enemyName);

        // Award loot
        Item loot = currentEnemy.dropLoot(dice);
        if (loot != null) {
            activePlayer.pickItem(loot);
            uiManager.getLogPanel().addMessage("The enemy dropped a " + loot.getName() + "!");
        }

        // Give the player some gold for winning the fight
        activePlayer.addGold(currentEnemy.getGoldValue());
        uiManager.getLogPanel().addMessage("You found " + currentEnemy.getGoldValue() + " gold!");

        // Check if the player died from a special ability (like Lucy's)
        if (!activePlayer.isAlive()) {
            endGame();
        } else {
            this.currentEnemy = null;
            this.combatManager = null;
            uiManager.setDoorMode();
            updateGUI();
        }
    }

    private void fleeEncounter() {
        if (combatManager == null) return;

        // Delegate flee logic to the CombatManager
        CombatManager.FleeResult result = combatManager.attemptFlee();
        uiManager.getLogPanel().addMessage(result.logMessage);

        if (result.success) {
            String fledEnemyName = currentEnemy.getName();
            this.currentEnemy = null;
            this.combatManager = null;
            uiManager.setDoorMode(false); // Go to door mode, but don't clear the enemy image yet
            // Simulate choosing another door, ensuring a different enemy appears.
            int nextDoor = dice.roll(2);
            chooseDoor(nextDoor, fledEnemyName);
        } else {
            // Flee failed, check if player survived the enemy's attack
            if (!activePlayer.isAlive()) {
                endGame();
            }
            updateGUI();
        }
    }

    private void manageInventory() {
        // Delegate to ItemManager
        ItemManager.ItemUseResult result = itemManager.openInventoryDialog(activePlayer);
        if (!result.cancelled()) {
            if (result.message() != null && !result.message().isEmpty()) {
                uiManager.getLogPanel().addMessage(result.message());
            }
            if (result.success()) {
                updateGUI();
            }
        }
    }

    public void updateGUI() {
        uiManager.updateAllPanels(activePlayer, party, currentEnemy);
    }

    private void endGame() {
        uiManager.getLogPanel().addMessage("\nYou have been defeated! GAME OVER.");
        uiManager.displayImage("images/ui/GameOver.png");
        uiManager.setGameOverMode();
    }

    private void winGame() {
        uiManager.getLogPanel().addMessage("\nCONGRATULATIONS! You have escaped the dungeon!");
        uiManager.displayImage("images/ui/Victory.png");
        uiManager.setGameOverMode();
    }
}