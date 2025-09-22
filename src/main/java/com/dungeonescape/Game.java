package com.dungeonescape;

import java.util.Random;
import javax.swing.JOptionPane;

import exceptions.InvalidMoveException;
import view.ControlPanel;
import view.DungeonPanel;
import view.HUDPanel;
import view.InventoryPanel;
import view.LogPanel;
import model.Item;
import model.Enemy;
import model.EnemyFactory;
import model.Player;
import model.Potion;
import model.Weapon;

/**
 * The main controller for the game, managing the game state and main loop.
 */
public class Game {
    // Models
    private Player player;
    private EnemyFactory enemyFactory;
    private Random random;
    private Enemy currentEnemy;

    // Views
    private DungeonPanel dungeonPanel;
    private InventoryPanel inventoryPanel;
    private LogPanel logPanel;
    private ControlPanel controlPanel;
    private HUDPanel hudPanel;

    public Game(DungeonPanel dungeonPanel, InventoryPanel inventoryPanel, LogPanel logPanel, ControlPanel controlPanel, HUDPanel hudPanel) {
        // Models
        this.player = new Player("Hero");
        this.enemyFactory = new EnemyFactory();
        this.random = new Random();

        // Views
        this.dungeonPanel = dungeonPanel;
        this.inventoryPanel = inventoryPanel;
        this.logPanel = logPanel;
        this.controlPanel = controlPanel;
        this.hudPanel = hudPanel;

        // Link controller to views by adding action listeners to buttons
        this.controlPanel.exploreButton.addActionListener(e -> explore());
        this.controlPanel.inventoryButton.addActionListener(e -> manageInventory());
        this.controlPanel.attackButton.addActionListener(e -> performCombatRound());

        // Start the game
        startGame();
    }

    /**
     * Initializes the player with starting items and sets up the game state.
     */
    private void startGame() {
        logPanel.addMessage("Welcome to Dungeon Escape!");
        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);
        player.pickItem(sword);
        logPanel.addMessage("Sword added to inventory.");
        player.pickItem(potion);
        logPanel.addMessage("Health Potion added to inventory.");

        logPanel.addMessage("\nYou start your journey with a sword and a potion.");
        try {
            useItem("Sword"); // Auto-equip sword
        } catch (InvalidMoveException e) {
            logPanel.addMessage("Critical setup error: " + e.getMessage());
        }
        updateGUI();
    }

    private void updateGUI() {
        inventoryPanel.updateInventory(player);
        hudPanel.updateStatus(player);
        // In the future, you could update the map as well
        // dungeonPanel.updateMap(player.getPosition());
    }

    /**
     * Handles the logic for exploring, which can lead to finding an enemy, an item, or nothing.
     */
    private void explore() {
        logPanel.addMessage("\nYou venture deeper into the dungeon...");
        int eventChance = random.nextInt(10); // 0-9

        if (eventChance < 6) { // 60% chance to find an enemy
            enterCombat(enemyFactory.createRandomEnemy());
        } else if (eventChance < 9) { // 30% chance to find an item (pickItem)
            logPanel.addMessage("You found a treasure chest!");
            Potion foundPotion = new Potion("Lesser Potion", 25, 1);
            int choice = JOptionPane.showConfirmDialog(null,
                    "You found a '" + foundPotion.getName() + "'. Do you want to pick it up?",
                    "Treasure Found", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                player.pickItem(foundPotion);
                logPanel.addMessage("You picked up the " + foundPotion.getName() + ".");
            } else {
                logPanel.addMessage("You leave the potion behind.");
            }
        } else { // 10% chance for nothing
            logPanel.addMessage("The corridor is empty. You continue on your way.");
        }
        updateGUI();
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        logPanel.addMessage("\nA wild " + enemy.getName() + " appears! (Health: " + enemy.getHealth() + ")");

        // Update GUI for combat state
        controlPanel.exploreButton.setVisible(false);
        controlPanel.inventoryButton.setVisible(false);
        controlPanel.attackButton.setVisible(true);
        controlPanel.fleeButton.setVisible(true);
    }

    private void exitCombat() {
        this.currentEnemy = null;
        logPanel.addMessage("Combat is over.");

        // Update GUI for exploration state
        controlPanel.exploreButton.setVisible(true);
        controlPanel.inventoryButton.setVisible(true);
        controlPanel.attackButton.setVisible(false);
        controlPanel.fleeButton.setVisible(false);
    }

    private void performCombatRound() {
        if (currentEnemy == null || !player.isAlive()) {
            return;
        }

        // Player's turn
        String playerAttackResult = player.attack(currentEnemy);
        logPanel.addMessage(playerAttackResult);

        // Check if enemy is defeated
        if (!currentEnemy.isAlive()) {
            logPanel.addMessage("You defeated the " + currentEnemy.getName() + "!");
            exitCombat();
            updateGUI();
            return;
        }

        // Enemy's turn
        String enemyAttackResult = currentEnemy.attack(player);
        logPanel.addMessage(enemyAttackResult);

        // Check if player is defeated
        if (!player.isAlive()) {
            logPanel.addMessage("You have been defeated! GAME OVER.");
            // Disable all buttons
            controlPanel.exploreButton.setEnabled(false);
            controlPanel.inventoryButton.setEnabled(false);
            controlPanel.attackButton.setEnabled(false);
            controlPanel.fleeButton.setEnabled(false);
        }

        updateGUI();
    }

    /**
     * Manages the inventory screen, allowing the player to use items.
     */
    private void manageInventory() { // This is a private helper method
        // Create a list of item names for the dialog
        Object[] itemNames = player.getInventory().getItems().stream().map(Item::getName).toArray();

        if (itemNames.length == 0) {
            JOptionPane.showMessageDialog(null, "Your inventory is empty.");
            return;
        }

        String selectedItem = (String) JOptionPane.showInputDialog(null,
                "Choose an item to use:", "Inventory",
                JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);

        if (selectedItem != null && !selectedItem.isEmpty()) {
            try {
                useItem(selectedItem);
            } catch (InvalidMoveException e) {
                logPanel.addMessage("Could not use item: " + e.getMessage());
            }
        }
    }

    // This public method wraps the model logic with controller/view logic
    public void useItem(String itemName) throws InvalidMoveException {
        Item item = player.getInventory().findItem(itemName);
        player.useItem(itemName); // Tell the model to update its state

        // Now, tell the view what happened
        if (item instanceof Weapon) {
            logPanel.addMessage(player.getName() + " equipped " + itemName + ".");
        } else {
            logPanel.addMessage("Used " + itemName + ".");
        }
        updateGUI();
    }
}