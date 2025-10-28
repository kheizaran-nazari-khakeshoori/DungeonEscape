package controller;

import exceptions.InvalidMoveException;
import model.Item;
import model.Player;

public class ItemUsageManager {

    /**
     * Uses an item from player's inventory.
     * @param player The player using the item.
     * @param itemName The name of the item to use.
     * @return An ItemUseResult detailing the outcome.
     * @throws InvalidMoveException if the item cannot be found or used.
     */
    public ItemUseResult useItem(Player player, String itemName) throws InvalidMoveException {
        // Find item before using it
        Item usedItem = player.getInventory().findItem(itemName);
        if (usedItem == null) {
            throw new InvalidMoveException("Could not find item '" + itemName + "'");
        }

        // 1. Apply the item's effect to the player.
        // This is polymorphism: we call `use` on an `Item`, and the correct
        // implementation (Potion, Weapon, etc.) is executed.
        usedItem.use(player);

        // 2. If the item is consumable (like a potion), remove it from inventory.
        if (usedItem.isConsumable()) {
            player.getInventory().removeItem(usedItem);
        }

        // 3. Return the result, including the item's specific use message.
        return new ItemUseResult(usedItem.getUseMessage(player), true, false, false);
    }

    public record ItemUseResult(String message, boolean success, boolean cancelled, boolean error) {}
}