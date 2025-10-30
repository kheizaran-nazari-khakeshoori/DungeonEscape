package controller;

import exceptions.InvalidMoveException;
import model.Item;
import model.Player;

public class ItemUsageManager {

    public ItemUseResult useItem(Player player, String itemName) throws InvalidMoveException {
       
        Item usedItem = player.getInventory().findItem(itemName);
        if (usedItem == null) {
            throw new InvalidMoveException("Could not find item '" + itemName + "'");
        }

        usedItem.use(player);

        if (usedItem.isConsumable()) {
            player.getInventory().removeItem(usedItem);
        }

        return new ItemUseResult(usedItem.getUseMessage(player), true, false, false);
    }

    public record ItemUseResult(String message, boolean success, boolean cancelled, boolean error) {}
}