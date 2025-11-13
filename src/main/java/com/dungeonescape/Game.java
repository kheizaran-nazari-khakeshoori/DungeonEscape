package com.dungeonescape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.CombatManager;
import controller.DoorManager;
import controller.EncounterType;
import controller.ItemUsageManager;
import controller.LevelManager;
import controller.TrapManager;
import controller.TrapResult;
import exceptions.InvalidMoveException;
import model.Bean;
import model.Elfo;
import model.Enemy;
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
import view.InventoryDialog;
import view.InventoryPanel;
import view.LogPanel;
import view.PlayerListPanel;
import view.ShopDialog;
import view.UIStateManager;

//class is responsible for overall game flow and coordination (managing encounters, UI updates, game state)

public class Game {
    
    private final Player activePlayer;//association 
    private final List<Player> party;
    private Enemy currentEnemy;//association 

    // Managers (single responsibility classes)
    private final DiceRoller dice;
    private final DoorManager doorManager;
    private final TrapManager trapManager;
    private final LevelManager levelManager;
    private final ItemUsageManager itemUsageManager;
    private final UIStateManager uiManager;
    private CombatManager combatManager;
    private final ShopEncounter shopEncounter;


    private final Map<String, Integer> enemyEncounterCount;//the variable type is map (interface)

    public Game(Player player, GameWindow gameWindow, PlayerListPanel PlayerListPanel, DungeonPanel dungeonPanel, InventoryPanel inventoryPanel, LogPanel logPanel, ControlPanel controlPanel, HUDPanel hudPanel, ItemUsageManager itemUsageManager) {
        // Initialize player and party
        this.activePlayer = player;
        this.party = new ArrayList<>();
        initializeParty();
        // Initialize utilities
        this.dice = new DiceRoller();
        //instantiation 
        this.enemyEncounterCount = new HashMap<>();
        // Initialize managers
        TrapFactory trapFactory = new TrapFactory(dice);
        this.doorManager = new DoorManager(dice, enemyEncounterCount);
        this.trapManager = new TrapManager(trapFactory, dice);
        this.levelManager = new LevelManager(dice);
        this.itemUsageManager = itemUsageManager;
        this.uiManager = new UIStateManager(dungeonPanel, controlPanel, inventoryPanel,
                                            hudPanel, PlayerListPanel, logPanel);
        this.shopEncounter = new ShopEncounter();
        // Setup UI listeners
        setupListeners(dungeonPanel, controlPanel);
        // Start the game
        startGame();
    }

    private void initializeParty() {
        this.party.add(this.activePlayer);
        List<Player> allCharacters = new ArrayList<>();
        allCharacters.add(new Elfo());
        allCharacters.add(new Bean());
        allCharacters.add(new Lucy());

        for (Player character : allCharacters)
        {
            if (!character.getClass().equals(activePlayer.getClass())) 
            {
                party.add(character);
            }
        }
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
                ItemUsageManager.ItemUseResult result = itemUsageManager.useItem(activePlayer, startingWeapon.getName());
                uiManager.getLogPanel().addMessage(result.message());
            } catch (InvalidMoveException e) {
                uiManager.getLogPanel().addMessage("Error equipping starting weapon: " + e.getMessage());
            }
        }
    }

    private void startNextLevel() {
        Level<Enemy> level = levelManager.getCurrentLevel();
        doorManager.setEncounters(levelManager.getRandomizedEnemySuppliers());
        uiManager.loadBackgroundImage(level.getBackgroundImagePath());
        uiManager.getLogPanel().addMessage("\n--- You have entered " + level.getName() + " ---");
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


        var encounterType = encounter.type();
        if (encounterType == EncounterType.ENEMY) {
            handleEnemyEncounter(encounter.enemy());
        } else if (encounterType == EncounterType.TRAP) {
            handleTrapEncounter();
        } else if (encounterType ==EncounterType.EMPTY_ROOM) {
            handleEmptyRoom();
        } else if (encounterType ==EncounterType.LEVEL_COMPLETE) {
            handleLevelComplete();
        }
    }

    private void handleEnemyEncounter(Enemy enemy) {
        enterCombat(enemy);
    }
    

    private void handleTrapEncounter() {
        // Delegate to TrapManager
        TrapResult result = trapManager.handleTrap(activePlayer,true);
        uiManager.getLogPanel().addMessage("\n=== TRAP ENCOUNTERED ===");
        uiManager.displayImage(result.imagePath());
        uiManager.getLogPanel().addMessage(result.messages());
        uiManager.getLogPanel().addMessage("========================\n");
        updateGUI();

        if (result.playerDied()) {
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
        } 
        else if (!currentEnemy.isAlive())
        {
            winCombat();
        }
        else 
        {
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
        
        } else if (!currentEnemy.isAlive()) {
            winCombat();
    
        } else {
            uiManager.updateSpecialAbilityButton(activePlayer);
            updateGUI();
        }
    }

    private void winCombat() {
        String enemyName = currentEnemy.getName();
        uiManager.getLogPanel().addMessage("You defeated the " + enemyName + "!");

        // Update encounter count
        doorManager.Enemy_Appearance_count(enemyName);

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
        uiManager.getLogPanel().addMessage(result.logMessage());

        if (result.success()) {
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
        // UI logic is now handled in the Game controller, not the ItemManager.
        // 1. Get data from the model
        List<String> itemNamesList = activePlayer.getInventory().getItemNames();
        // 2. Delegate UI interaction to a dedicated View component
        InventoryDialog inventoryDialog = new InventoryDialog();
        String selectedItem = inventoryDialog.showAndGetSelectedItem(itemNamesList);

        // If the user cancelled
        // 3. Process the result from the view
        if (selectedItem == null || selectedItem.isEmpty()) {
            return;
        }

        // Try to use the item by calling the dedicated manager
        try {
            ItemUsageManager.ItemUseResult result = itemUsageManager.useItem(activePlayer, selectedItem);
            uiManager.getLogPanel().addMessage(result.message());
            if (result.success()) {
                updateGUI(); // Update UI only on successful use
            }
        } catch (InvalidMoveException e) {
            uiManager.getLogPanel().addMessage("Could not use item: " + e.getMessage());
        }
    }

//seperation of concern design pattern 
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