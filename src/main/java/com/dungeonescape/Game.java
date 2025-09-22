package com.dungeonescape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import exceptions.InvalidMoveException;
import model.Bean;
import model.Elfo;
import model.Enemy;
import model.EnemyFactory;
import model.Item;
import model.Lucy;
import model.Player;
import model.Potion;
import model.Weapon;
import utils.DiceRoller;
import view.ControlPanel;
import view.DungeonPanel;
import view.GameWindow;
import view.HUDPanel;
import view.InventoryPanel;
import view.LogPanel;
import view.PartyPanel;

/**
 * The main controller for the game, managing the game state and main loop.
 */
public class Game {
    // Models
    private Player activePlayer;
    private final List<Player> party;
    private final Map<String, Integer> enemyEncounterCount;
    private List<Supplier<Enemy>> encounterDeck;
    private EnemyFactory enemyFactory;
    private DiceRoller dice;
    private Enemy currentEnemy;

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
        this.enemyEncounterCount = new HashMap<>();
        this.encounterDeck = new ArrayList<>();

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
        this.controlPanel.attackButton.addActionListener(e -> performCombatRound());
        this.controlPanel.fleeButton.addActionListener(e -> fleeEncounter());
        this.controlPanel.inventoryButton.addActionListener(e -> manageInventory());
        this.controlPanel.exitButton.addActionListener(e -> System.exit(0));

        // Start the game
        startGame();
    }

    /**
     * Initializes the player with starting items and sets up the game state.
     */
    private void startGame() {
        logPanel.addMessage("Welcome to Dungeon Escape! Your adventure begins.");
        logPanel.addMessage("You enter a dark dungeon. Choose a door to proceed.");

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
        resetAndShuffleEncounterDeck();
        setDoorMode();
    }

    /**
     * Refills the encounter deck with all possible enemies and shuffles it.
     */
    private void resetAndShuffleEncounterDeck() {
        this.encounterDeck = enemyFactory.getEnemySuppliers();
        Collections.shuffle(this.encounterDeck, this.dice.getRandom());
    }

    private void updateGUI() {
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

        if (encounterDeck.isEmpty()) {
            logPanel.addMessage("You feel a shift in the dungeon's malevolent energy...");
            resetAndShuffleEncounterDeck();
        }

        // Get the next enemy from our shuffled "deck"
        Supplier<Enemy> enemySupplier = encounterDeck.remove(0);
        Enemy enemy = enemySupplier.get();

        // If we are fleeing and the next enemy is the same type, try to swap it
        // for the next one in the deck to improve variety.
        if (enemyToAvoid != null && enemy.getName().equals(enemyToAvoid) && !encounterDeck.isEmpty()) {
            logPanel.addMessage("...but you manage to dodge into a different corridor!");
            Supplier<Enemy> nextSupplier = encounterDeck.remove(0);
            // Put the avoided enemy back at the end of the deck
            encounterDeck.add(enemySupplier);
            enemy = nextSupplier.get();
        }
        
        String enemyName = enemy.getName();

        // Strengthen enemy if it has been seen before
        int encounterLevel = enemyEncounterCount.getOrDefault(enemyName, 0);
        if (encounterLevel > 0) {
            enemy.strengthen(encounterLevel);
            logPanel.addMessage("This " + enemyName + " seems stronger than the last one!");
        }

        enterCombat(enemy);
    }

    private Item generateLoot() {
        // Tiered loot based on progression
        // For now, we keep it simple. This can be expanded later.
        return new Potion("Health Potion", "A common potion that restores 25 HP.", 25, 1, "images/potions/HealthPotion.png"); // Default common loot
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        logPanel.addMessage("\nA " + enemy.getName() + " (Lvl " + (enemyEncounterCount.getOrDefault(enemy.getName(), 0) + 1) + ") appears!");
        logPanel.addMessage(enemy.getHint()); // Display the hint
        setCombatMode();
        updateGUI();
        dungeonPanel.displayImage(enemy.getImagePath());
    }

    private void setCombatMode() {
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.attackButton.setVisible(true);
        controlPanel.fleeButton.setVisible(true);
    }

    private void setDoorMode() {
        this.currentEnemy = null;
        dungeonPanel.clearImage();
        dungeonPanel.door1Button.setVisible(true);
        dungeonPanel.door2Button.setVisible(true);
        controlPanel.attackButton.setVisible(false);
        controlPanel.fleeButton.setVisible(false);
        updateGUI();
    }

    private void performCombatRound() {
        if (currentEnemy == null || !activePlayer.isAlive()) {
            return;
        }

        // Player's turn
        activePlayer.applyTurnEffects(); // Apply healing over time, etc.
        String playerAttackResult = activePlayer.attack(currentEnemy);
        logPanel.addMessage(playerAttackResult);

        // Check if enemy is defeated
        if (!currentEnemy.isAlive()) {
            logPanel.addMessage("You defeated the " + currentEnemy.getName() + "!");

            // Update encounter count
            String enemyName = currentEnemy.getName();
            int newCount = enemyEncounterCount.getOrDefault(enemyName, 0) + 1;
            enemyEncounterCount.put(enemyName, newCount);

            // Award loot
            Item loot = generateLoot();
            activePlayer.pickItem(loot);
            logPanel.addMessage("The enemy dropped a " + loot.getName() + "!");

            setDoorMode();
            return;
        }

        // Enemy's turn
        String enemyAttackResult = currentEnemy.attack(activePlayer);
        logPanel.addMessage(enemyAttackResult);

        // Check if player is defeated
        if (!activePlayer.isAlive()) {
            endGame();
        }

        updateGUI();
    }

    private void fleeEncounter() {
        if (currentEnemy == null) return;

        logPanel.addMessage("You flee from the battle and rush through another door...");

        String fledEnemyName = currentEnemy.getName();

        // To trigger a new encounter, we must first clear the current one.
        this.currentEnemy = null;

        // Simulate choosing another door, ensuring a different enemy appears.
        // The door number (1 or 2) only affects the log message, so picking one randomly is fine.
        int nextDoor = dice.roll(2);
        chooseDoor(nextDoor, fledEnemyName);
    }

    private void endGame() {
        logPanel.addMessage("\nYou have been defeated! GAME OVER.");
        
        // Disable all buttons
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.attackButton.setEnabled(false);
        controlPanel.fleeButton.setEnabled(false);
        controlPanel.inventoryButton.setEnabled(false);

        // Display a game over image or message
        dungeonPanel.displayImage("images/ui/GameOver.png"); // Assuming you have this image
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
     * This private method is the single point of control for using an item.
     * It updates the model and then updates the view.
     */
    private void processUseItem(String itemName) throws InvalidMoveException {
        Item item = activePlayer.getInventory().findItem(itemName);
        if (item == null) {
            throw new InvalidMoveException("Item '" + itemName + "' not found!");
        }

        // Let the item affect the player (Polymorphism)
        item.use(activePlayer);

        // Handle logging based on item type
        if (item instanceof Weapon) {
            logPanel.addMessage(activePlayer.getName() + " equipped " + itemName + ".");
        } else {
            logPanel.addMessage("Used " + itemName + ".");
        }

        // If the item is consumable, remove it from the model's inventory
        if (item.isConsumable()) {
            activePlayer.getInventory().removeItem(item);
        }

        updateGUI(); // Refresh the screen
    }
}