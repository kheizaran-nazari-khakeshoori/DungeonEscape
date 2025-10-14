package controller;

import javax.swing.JOptionPane;

import exceptions.InvalidMoveException;
import model.Item;
import model.Player;

public class ItemManager {
    /**
     * Opens inventory dialog and processes item usage.
     * @return ItemUseResult containing messages and status
     */
    public ItemUseResult openInventoryDialog(Player player) {
        // Create list of item names
        Object[] itemNames = player.getInventory().getItems().stream()
            .map(Item::getName)
            .toArray();
        if (itemNames.length == 0) {
            JOptionPane.showMessageDialog(null, "Your inventory is empty.");
            return new ItemUseResult(null, false, true, false);
        }
        String selectedItem = (String) JOptionPane.showInputDialog(null,
                "Choose an item to use:", "Inventory",
                JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);
        if (selectedItem == null || selectedItem.isEmpty()) {
            return new ItemUseResult(null, false, true, false);
        }
        try {
            return useItem(player, selectedItem);
        } catch (InvalidMoveException e) {
            return new ItemUseResult("Could not use item: " + e.getMessage(), false, false, true);
        }
    }

    /**
     * Uses an item from player's inventory.
     */
    public ItemUseResult useItem(Player player, String itemName) throws InvalidMoveException {
        // Find item before using it
        Item usedItem = player.getInventory().findItem(itemName);
        if (usedItem == null) {
            throw new InvalidMoveException("Could not find item '" + itemName + "'");
        }
        // Use the item
        player.useItem(itemName);
        // Get the use message (polymorphism!)
        return new ItemUseResult(usedItem.getUseMessage(player), true, false, false);
    }

    public record ItemUseResult(String message, boolean success, boolean cancelled, boolean error) {}
}