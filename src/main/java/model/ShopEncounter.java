package model;

/**
 * Represents an encounter with a shop.
 */
public class ShopEncounter {
    private Shop shop;
    private String enterMessage;

    public ShopEncounter() {
        this.shop = new Shop();
        this.enterMessage = "You stumble upon a shop! A shady merchant awaits...";
    }

    public String getEnterMessage() {
        return enterMessage;
    }

    public Shop getShop() {
        return shop;
    }

    // Example interaction: Buying an item
    public boolean purchaseItem(Player player, String itemName) {
        return shop.sellItem(player, itemName);
    }

    // You might want to add methods for selling items to the shop
    // public boolean sellItem(Player player, String itemName) { ... }

    /**
     * Example of displaying the shop's inventory.
     * This is just a helper method; the actual display would be handled by the UI.
     */
    public String displayShopInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Shop Inventory ---").append("\n");
        for (Item item : shop.getInventory()) {
            sb.append("- ").append(item.getName()).append("\n");
        }
        return sb.toString();
    }
}