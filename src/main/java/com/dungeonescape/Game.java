package com.dungeonescape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import controller.RuleEngine;
import exceptions.InvalidMoveException;
import model.Bean;
import model.Elfo;
import model.Enemy;
import model.EnemyFactory;
import model.Ghost;
import model.Goblin;
import model.InvisibilityEffect;
import model.Item;
import model.Level;
import model.Lucy;
import model.MimicChest;
import model.Player;
import model.PoisonSpider;
import model.ShadowAssassin;
import model.ShopEncounter;
import model.SkeletonWarrior;
import model.SlimeBlob;
import model.StoneMan;
import model.Trap;
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
 * The main controller for the game, managing the game state and main loop.
 */
public class Game {
    // Models
    private Player activePlayer;
    private final List<Player> party;
    private final Map<String, Integer> enemyEncounterCount;
    private List<Supplier<Enemy>> currentEncounterDeck;
    private EnemyFactory enemyFactory;
    private TrapFactory trapFactory;
    private DiceRoller dice;
    private RuleEngine ruleEngine;
    private Enemy currentEnemy;
    private ShopEncounter shopEncounter;
    private List<Level<Enemy>> gameLevels;
    private int currentLevelIndex;

    // Views
    private final PartyPanel partyPanel;
    private DungeonPanel dungeonPanel;
    private InventoryPanel inventoryPanel;
    private LogPanel logPanel;
    private ControlPanel controlPanel;
    private HUDPanel hudPanel;

    public Game(Player player, GameWindow gameWindow, PartyPanel partyPanel, DungeonPanel dungeonPanel, InventoryPanel inventoryPanel, LogPanel logPanel, ControlPanel controlPanel, HUDPanel hudPanel) {
        // Models
        this.party = new ArrayList<>();
        this.party.add(new Elfo());
        this.party.add(new Bean());
        this.party.add(new Lucy());

        // Find the player in the party that matches the class of the one selected
        // in the previous screen. This ensures the activePlayer is the same instance
        // as the one in the party list, which is important for the UI.
        this.activePlayer = party.stream()
                .filter(p -> p.getClass().equals(player.getClass()))
                .findFirst()
                .orElse(party.get(0)); // Fallback to the first player if not found

        // CRITICAL FIX: Transfer the inventory from the temporary player object (which has
        // the selected items) to the actual player instance being used in the game.
        for (Item item : player.getInventory().getItems()) {
            this.activePlayer.pickItem(item);
        }

        this.dice = new DiceRoller();
        this.enemyFactory = new EnemyFactory(this.dice);
        this.trapFactory = new TrapFactory(this.dice);
        this.enemyEncounterCount = new HashMap<>();
        this.ruleEngine = new RuleEngine();
        this.currentEncounterDeck = new ArrayList<>();
        this.gameLevels = new ArrayList<>();
        this.shopEncounter = new ShopEncounter(); // Create a single shop instance for the game
        this.currentLevelIndex = 0;

        // Views
        this.partyPanel = partyPanel;
        this.dungeonPanel = dungeonPanel;
        this.inventoryPanel = inventoryPanel;
        this.logPanel = logPanel;
        this.controlPanel = controlPanel;
        this.hudPanel = hudPanel;

        // Link controller to views by adding action listeners to buttons
        this.dungeonPanel.door1Button.addActionListener(e -> chooseDoor(1));
        this.dungeonPanel.door2Button.addActionListener(e -> chooseDoor(2));
        this.controlPanel.addAttackListener(e -> performCombatRound());
        this.controlPanel.addSpecialListener(e -> useSpecialAbility());
        this.controlPanel.addFleeListener(e -> fleeEncounter());
        this.controlPanel.addInventoryListener(e -> manageInventory());
        this.controlPanel.addContinueListener(e -> setDoorMode());
        this.controlPanel.addContinueListener(e -> setDoorMode());
        this.controlPanel.addShopListener(e -> openShop());
        this.controlPanel.addExitListener(e -> System.exit(0));

        // Start the game
        startGame();
    }

    /**
     * Initializes the player with starting items and sets up the game state.
     */
    private void startGame() {
        logPanel.addMessage("Welcome to Dungeon Escape!");
        initializeLevels();

        // Find the weapon the player chose during setup and equip it.
        Item startingWeapon = activePlayer.getInventory().getItems().stream()
                .filter(item -> item instanceof Weapon)
                .findFirst()
                .orElse(null);

        if (startingWeapon != null) {
            try {
                // The processUseItem method for a weapon equips it.
                processUseItem(startingWeapon.getName());
            } catch (InvalidMoveException e) {
                logPanel.addMessage("Error equipping starting weapon: " + e.getMessage());
            }
        }
        startNextLevel();
        
    }

    // Public getter for activePlayer, needed by ShopDialog
    public Player getActivePlayer() {
        return activePlayer;
    }
    // Public getter for logPanel, needed by ShopDialog
    public LogPanel getLogPanel() {
        return logPanel;
    }
    /**
     * Creates the sequence of levels for the game, defining the progression.
     */
    private void initializeLevels() {
        gameLevels = new ArrayList<>();
        // Level: Name, Background Image Path, List of Enemies
        // Level 1: The Goblin Warrens - Weaker enemies
        gameLevels.add(new Level<>("The Goblin Warrens", "images/ui/TwoDoors.png", List.of(Goblin::new, Goblin::new, SkeletonWarrior::new)));
        // Level 2: The Haunted Halls - More spectral enemies
        gameLevels.add(new Level<>("The Haunted Halls", "images/ui/HauntedHalls.png", List.of(SkeletonWarrior::new, Ghost::new, Ghost::new, ShadowAssassin::new)));
        // Level 3: The Creature Caves - Tougher, more exotic monsters
        gameLevels.add(new Level<>("The Creature Caves", "images/ui/CreatureCaves.png", List.of(PoisonSpider::new, SlimeBlob::new, StoneMan::new, MimicChest::new)));
        // In the future, you could add a final level with a unique Boss.
    }

    private void startNextLevel() {
        Level<Enemy> level = gameLevels.get(currentLevelIndex);
        dungeonPanel.loadBackgroundImage(level.getBackgroundImagePath());
        logPanel.addMessage("\n--- You have entered " + level.getName() + " ---");
        this.currentEncounterDeck = level.getShuffledDeck(dice);
        setDoorMode();
    }

    public void updateGUI() {
        inventoryPanel.updateInventory(activePlayer);
        hudPanel.updateStatus(activePlayer);
        hudPanel.updateEnemyStatus(currentEnemy);
        partyPanel.updateParty(activePlayer, party);
    }

    /**
     * Handles the logic for exploring, which can lead to finding an enemy, an item, or nothing.
     */
    private void chooseDoor(int doorNumber) { // Overloaded for simplicity
        chooseDoor(doorNumber, null);
    }

    /**
     * Main logic for choosing a door, with an option to avoid a specific enemy type.
     * @param doorNumber The door chosen (1 or 2).
     * @param enemyToAvoid The name of an enemy to not generate, or null.
     */
    private void chooseDoor(int doorNumber, String enemyToAvoid) {
        if (currentEnemy != null) return; // Can't open a door during combat

        String doorName = (doorNumber == 1) ? "left" : "right";
        logPanel.addMessage("\nYou open the " + doorName + " door...");

        // --- Corrected Encounter Logic ---
        // Roll the dice ONCE to determine the outcome.
        double encounterRoll = dice.getRandom().nextDouble();
        double enemyChance = ruleEngine.getEnemyChance();

        if (encounterRoll < enemyChance) { // e.g., 0.0 to 0.7
            if (currentEncounterDeck.isEmpty()) {
                logPanel.addMessage("You've cleared this section of the dungeon!");
                currentLevelIndex++;
                if (currentLevelIndex >= gameLevels.size()) {
                    winGame(); // Player has beaten all levels
                } else {
                    startNextLevel();
                }
                return;
            }

            // Get the next enemy from our shuffled "deck"
            Supplier<Enemy> enemySupplier = currentEncounterDeck.remove(0);
            Enemy enemy = enemySupplier.get();

            // If we are fleeing and the next enemy is the same type, try to swap it
            // for the next one in the deck to improve variety.
            if (enemyToAvoid != null && enemy.getName().equals(enemyToAvoid) && !currentEncounterDeck.isEmpty()) {
                logPanel.addMessage("...but you manage to dodge into a different corridor!");
                Supplier<Enemy> nextSupplier = currentEncounterDeck.remove(0);
                // Put the avoided enemy back at the end of the deck
                currentEncounterDeck.add(enemySupplier);
                enemy = nextSupplier.get();
            }
            
            String enemyName = enemy.getName();

            // Strengthen enemy if it has been seen before
            int encounterLevel = enemyEncounterCount.getOrDefault(enemyName, 0);
            if (encounterLevel > 0) {
                enemy.strengthen(encounterLevel, this.ruleEngine);
                logPanel.addMessage("This " + enemyName + " seems stronger than the last one!");
            }

            enterCombat(enemy);
        } else if (encounterRoll < enemyChance + ruleEngine.getTrapChance()) { // e.g., 0.65 to 0.80
            handleTrapEncounter();
        } else {
            // This is the "nothing happens" case.
            try {
                dungeonPanel.displayImage("images/ui/EmptyRoom.png");
            } catch (Exception e) {
                logPanel.addMessage("[SYSTEM] Warning: Could not load image for Empty Room. Error: " + e.getClass().getSimpleName());
            }
            logPanel.addMessage("The room is empty. You proceed to the next choice of doors.");
            setPostEncounterMode(); // Show image before doors reappear
        }
    }

    private void handleTrapEncounter() {
        try {
            dungeonPanel.displayImage("images/ui/Trap.png");
        } catch (Exception e) {
            // Gracefully handle missing image files instead of crashing.
            logPanel.addMessage("[SYSTEM] Warning: Could not load image for Trap. Error: " + e.getClass().getSimpleName());
        }

        Trap trap = trapFactory.createRandomTrap();
        logPanel.addMessage(trap.getTriggerMessage());

        int disarmChancePercent = (int) (activePlayer.getTrapDisarmChance() * 100);
        int choice = JOptionPane.showConfirmDialog(
            null,
            "A " + trap.getName() + "!\nAttempt to disarm/avoid it? (Chance: " + disarmChancePercent + "%)",
            "It's a Trap!",
            JOptionPane.YES_NO_OPTION
        );

        boolean attemptDisarm = (choice == JOptionPane.YES_OPTION);

        if (attemptDisarm && dice.getRandom().nextDouble() < activePlayer.getTrapDisarmChance()) {
            // Success!
            logPanel.addMessage("Success! You deftly avoid the " + trap.getName() + ".");
            setPostEncounterMode(); // Show image before doors reappear
        } else {
            // Failure or chose not to attempt
            if (attemptDisarm) {
                logPanel.addMessage("You failed to disarm the trap!");
            }
            String result = trap.trigger(activePlayer);
            logPanel.addMessage(result);
            if (!activePlayer.isAlive()) {
                endGame();
            } else {
                setPostEncounterMode();
            }
        }
    }

    private void openShop() {
        if (currentEnemy != null) return; // Can't open shop during combat
        logPanel.addMessage(shopEncounter.getEnterMessage());

        // Open the shop UI
        ShopDialog shopDialog = new ShopDialog(this, shopEncounter);
        shopDialog.setVisible(true);
        // The game is modal, so it will pause here until the dialog is closed.
        logPanel.addMessage("You leave the shop.");
        updateGUI(); // Refresh GUI in case gold or items changed
    }

    private Item generateLoot() {
        // TODO: Implement a more dynamic loot system.
        // For now, return null to prevent a potion from dropping after every fight.
        return null;
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        logPanel.addMessage("\nA " + enemy.getName() + " (Lvl " + (enemyEncounterCount.getOrDefault(enemy.getName(), 0) + 1) + ") appears!");
        logPanel.addMessage(enemy.getHint()); // Display the hint
        setCombatMode();
        updateGUI();
        try {
            dungeonPanel.displayImage(enemy.getImagePath());
        } catch (Exception e) { // Catching a broad exception is okay for logging, but let's be more specific in the message.
            // Gracefully handle missing image files instead of crashing.
            logPanel.addMessage("[SYSTEM] Warning: Could not load image for " + enemy.getName() + ". Error: " + e.getClass().getSimpleName());
        }
    }

    private void setCombatMode() {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAttackButtonVisible(true);
        controlPanel.setFleeButtonVisible(true);
        controlPanel.setSpecialButtonVisible(true);
        controlPanel.setShopButtonVisible(false);
    }

    private void setDoorMode() {
        setDoorMode(true); // By default, clear the image
    }

    /**
     * Sets the UI to door selection mode.
     * @param clearImage whether to clear the central image panel.
     */
    private void setDoorMode(boolean clearImage) {
        this.currentEnemy = null;
        if (clearImage) {
            dungeonPanel.clearImage();
        }
        dungeonPanel.door1Button.setVisible(true);
        dungeonPanel.door2Button.setVisible(true);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setContinueButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(true);
        updateGUI();
    }

    private void performCombatRound() {
        if (currentEnemy == null || !activePlayer.isAlive()) {
            return;
        }

        // This is a new button for continuing after a non-combat encounter
        if (controlPanel.isContinueButtonVisible()) {
            return;
        }

        // Player's turn
        String turnEffectsResult = activePlayer.applyTurnEffects(); // Renamed method in Player
        if (!turnEffectsResult.isEmpty()) {
            logPanel.addMessage(turnEffectsResult);
        }

        // Check if player is still alive after effects like poison
        // This is the critical fix: we must update the GUI and check for game over
        // immediately after effects are applied.
        if (!activePlayer.isAlive()) {
            endGame();
            return;
        }

        String playerAttackResult = activePlayer.attack(currentEnemy, dice);

        logPanel.addMessage(playerAttackResult);

        // Check if enemy is defeated
        if (!currentEnemy.isAlive()) {
            winCombat();
            return;
        }

        // Enemy's turn
        String enemyAttackResult = currentEnemy.attack(activePlayer, dice);
        logPanel.addMessage(enemyAttackResult);

        // Check if player is defeated
        if (!activePlayer.isAlive()) {
            endGame();
        }

        updateGUI();
    }

    private void useSpecialAbility() {
        if (currentEnemy == null || !activePlayer.isAlive()) {
            return;
        }

        // Player uses their special ability
        String abilityResult = activePlayer.useSpecialAbility(currentEnemy, dice);
        logPanel.addMessage(abilityResult);

        // Check if the ability defeated the enemy or the player
        if (!currentEnemy.isAlive()) {
            winCombat();
        } else if (!activePlayer.isAlive()) {
            endGame();
        } else {
            // If the fight continues, the enemy gets its turn
            enemyTurn();
        }
        // Update the GUI to reflect any changes (like Lucy's health drop or Bean's heal)
        updateGUI();
    }

    /**
     * Sets the UI to a state where the player has finished an encounter
     * (like a trap or an empty room) and needs to click "Continue" to proceed.
     * This allows the player to see the outcome and the associated image.
     */
    private void setPostEncounterMode() {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAttackButtonVisible(false);
        controlPanel.setFleeButtonVisible(false);
        controlPanel.setSpecialButtonVisible(false);
        controlPanel.setShopButtonVisible(false);
        controlPanel.setContinueButtonVisible(true);
    }

    private void winCombat() {
        logPanel.addMessage("You defeated the " + currentEnemy.getName() + "!");

        // Update encounter count
        String enemyName = currentEnemy.getName();
        int newCount = enemyEncounterCount.getOrDefault(enemyName, 0) + 1;
        enemyEncounterCount.put(enemyName, newCount);

        // Award loot
        Item loot = generateLoot();
        if (loot != null) {
            activePlayer.pickItem(loot);
            logPanel.addMessage("The enemy dropped a " + loot.getName() + "!");
        }

        // Give the player some gold for winning the fight
        activePlayer.addGold(currentEnemy.getGoldValue());
        logPanel.addMessage("You found " + currentEnemy.getGoldValue() + " gold!");

        // Check if the player died from a special ability (like Lucy's)
        if (!activePlayer.isAlive()) {
            endGame();
        } else {
            setDoorMode();
        }
    }

    private void fleeEncounter() {
        if (currentEnemy == null) return;

        boolean guaranteedFlee = activePlayer.hasEffect(InvisibilityEffect.EFFECT_NAME);
        // Use RuleEngine for flee chance
        if (guaranteedFlee || dice.getRandom().nextDouble() < ruleEngine.getFleeChance()) {
            if (guaranteedFlee) {
                logPanel.addMessage("You vanish from sight and easily escape!");
                activePlayer.removeEffect(InvisibilityEffect.EFFECT_NAME); // Effect is consumed on use
            } else {
                logPanel.addMessage("You successfully flee from the battle and rush through another door...");
            }

            String fledEnemyName = currentEnemy.getName();
            this.currentEnemy = null; // Clear the current combat

            // Simulate choosing another door, ensuring a different enemy appears.
            int nextDoor = dice.roll(2);
            chooseDoor(nextDoor, fledEnemyName);
        } else {
            logPanel.addMessage("You try to flee, but the " + currentEnemy.getName() + " blocks your path!");
            // Fleeing fails, so the enemy gets a free attack.
            enemyTurn();
        }
    }

    private void enemyTurn() {
        if (currentEnemy == null || !currentEnemy.isAlive()) return;
        String enemyAttackResult = currentEnemy.attack(activePlayer, dice);
        logPanel.addMessage(enemyAttackResult);
        if (!activePlayer.isAlive()) {
            endGame();
        }
        updateGUI();
    }

    private void endGame() {
        logPanel.addMessage("\nYou have been defeated! GAME OVER.");
        
        // Disable all buttons
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.setAllButtonsEnabled(false);

        // Display a game over image or message
        try {
            dungeonPanel.displayImage("images/ui/GameOver.png"); // Assuming you have this image
        } catch (Exception e) {
            logPanel.addMessage("[SYSTEM] Warning: Could not load GameOver image.");
        }
    }

    private void winGame() {
        logPanel.addMessage("\nCONGRATULATIONS! You have escaped the dungeon!");
        try {
            dungeonPanel.displayImage("images/ui/Victory.png"); // Assuming you have a victory image
        } catch (Exception e) {
            logPanel.addMessage("[SYSTEM] Warning: Could not load Victory image.");
        }
        controlPanel.setAllButtonsEnabled(false);
    }

    /**
     * Manages the inventory screen, allowing the player to use items.
     */
    private void manageInventory() { // This is a private helper method
        // Create a list of item names for the dialog
        Object[] itemNames = activePlayer.getInventory().getItems().stream().map(Item::getName).toArray();

        if (itemNames.length == 0) {
            JOptionPane.showMessageDialog(null, "Your inventory is empty.");
            return;
        }

        String selectedItem = (String) JOptionPane.showInputDialog(null,
                "Choose an item to use:", "Inventory",
                JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);

        if (selectedItem != null && !selectedItem.isEmpty()) {
            try {
                processUseItem(selectedItem);
            } catch (InvalidMoveException e) {
                logPanel.addMessage("Could not use item: " + e.getMessage());
            }
        }
    }

    /**
     * This method is the single point of control for using an item.
     * It updates the model and then updates the view.
     */
    private void processUseItem(String itemName) throws InvalidMoveException {
        // Find the item before it's potentially consumed by useItem().
        // This is safe because useItem() would have thrown an exception if it wasn't found.
        Item usedItem = activePlayer.getInventory().findItem(itemName);
        if (usedItem == null) { // Should not happen if logic is correct, but good for safety.
            throw new InvalidMoveException("Could not find item '" + itemName + "' to get use message.");
        }

        // Delegate the entire "use item" logic to the Player class.
        activePlayer.useItem(itemName);

        // Polymorphism in action! We ask the item itself for its use message.
        // The Game class no longer needs to know about specific item types.
        // The 'usedItem' variable from the top of the method holds the correct reference.
        logPanel.addMessage(usedItem.getUseMessage(activePlayer));
        updateGUI(); // Refresh the screen
    }
}