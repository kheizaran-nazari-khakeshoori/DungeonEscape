package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shop where the player can buy items.
 */
public class Shop {
    private List<Item> inventory;

    public Shop() {
        this.inventory = createShopInventory();
    }

    // Method to create the shop's initial inventory.
    private List<Item> createShopInventory() {
        List<Item> items = new ArrayList<>();

        // Add some potions
        items.add(new Potion("Health Potion", "A basic healing potion.", 30, 1, "images/potions/HealthPotion.png", 25));
        items.add(new StaminaElixir("Stamina Elixir", "Restores health and provides regeneration.", 20, "images/potions/StaminaElixir.png", 50));
        items.add(new InvisibilityPotion("Invisibility Potion", "Allows guaranteed escape from one encounter.", "images/potions/InvisibilityPotion.png", 75));

        // Add weapons
        items.add(new Weapon("Short Sword", "A basic but reliable weapon.", 10, 20, DamageType.SLASHING, "images/weapons/ShortSword.png", 40));
        items.add(new Weapon("Dagger", "A stealthy weapon.", 7, 30, DamageType.PIERCING, "images/weapons/Dagger.png", 30));
        // You can add more items here with their respective costs
        return items;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    // Method to handle selling an item to the player
    public boolean sellItem(Player player, String itemName) {
        Item itemToSell = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToSell = item;
                break;
            }
        }

        if (itemToSell == null) {
            System.out.println("Item not found in shop.");
            return false;
        }

        int itemCost = itemToSell.getCost(); // Correctly access the cost from the Item object
        if (player.getGold() >= itemCost) {
            player.addGold(-itemCost); // Charge the player
            player.pickItem(itemToSell); // Add item to player's inventory
            System.out.println(player.getName() + " bought " + itemToSell.getName() + " for " + itemCost + " gold.");
            return true;
        } else {
            System.out.println("Not enough gold to buy " + itemToSell.getName());
            return false;
        }
    }
}