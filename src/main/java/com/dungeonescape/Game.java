package com.dungeonescape;

import java.util.Random;
import javax.swing.JOptionPane;

import exceptions.InvalidMoveException;
import view.ControlPanel;
import view.GameWindow;
import view.DungeonPanel;
import view.HUDPanel;
import view.InventoryPanel;
import view.PartyPanel;
import view.LogPanel;
import model.Item;
import model.Enemy;
import model.Bean;
import model.Elfo;
import model.EnemyFactory;
import model.Lucy;
import model.Player;
import model.DamageType;
import model.Potion;
import model.Weapon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main controller for the game, managing the game state and main loop.
 */
public class Game {
    // Models
    private Player activePlayer;
    private final List<Player> party;
    private final Map<String, Integer> enemyEncounterCount;
    private EnemyFactory enemyFactory;
    private Random random;
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
        this.activePlayer = player;
        this.party = new ArrayList<>();
        this.party.add(player); // Start with Elfo
        this.party.add(new Bean()); // Add all players to the party list
        this.party.add(new Lucy());

        this.random = new Random();
        this.enemyFactory = new EnemyFactory(this.random);
        this.enemyEncounterCount = new HashMap<>();

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
        this.controlPanel.exitButton.addActionListener(e -> System.exit(0));

        // Start the game
        startGame();
    }

    /**
     * Initializes the player with starting items and sets up the game state.
     */
    private void startGame() {
        logPanel.addMessage("Welcome to Dungeon Escape!");
        logPanel.addMessage("Your journey begins. You have a sword but no potions.");

        // Auto-equip the first weapon in the inventory, if one exists.
        Item firstItem = activePlayer.getInventory().getItems().stream().findFirst().orElse(null);
        if (firstItem instanceof Weapon) {
            try {
                processUseItem(firstItem.getName());
            } catch (InvalidMoveException e) {
                logPanel.addMessage("Could not auto-equip starting weapon: " + e.getMessage());
            }
        }
        setDoorMode();
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
    private void chooseDoor(int doorNumber) {
        if (currentEnemy != null) return; // Can't open a door during combat

        String doorName = (doorNumber == 1) ? "left" : "right";
        logPanel.addMessage("\nYou open the " + doorName + " door...");

        Enemy enemy = enemyFactory.createRandomEnemy();
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
        return new Potion("Health Potion", 25, 1, "images/potions/HealthPotion.png"); // Default common loot
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        logPanel.addMessage("\nA " + enemy.getName() + " (Lvl " + (enemyEncounterCount.getOrDefault(enemy.getName(), 0) + 1) + ") appears!");
        logPanel.addMessage(enemy.getHint()); // Display the hint
        dungeonPanel.displayImage(enemy.getImagePath());
        setCombatMode();
        updateGUI();
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
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
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

        logPanel.addMessage("You flee from the battle, but the escape was costly.");
        activePlayer.takeDamage(5); // Apply the 5 HP penalty for fleeing

        if (!activePlayer.isAlive()) {
            endGame();
        } else {
            setDoorMode();
        }
    }

    private void endGame() {
        logPanel.addMessage("\nYou have been defeated! GAME OVER.");
        
        // Disable all buttons
        dungeonPanel.door1Button.setVisible(false);
        dungeonPanel.door2Button.setVisible(false);
        controlPanel.attackButton.setEnabled(false);
        controlPanel.fleeButton.setEnabled(false);

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