package model;

public abstract class Item {
    private String name;
    protected String imagePath;
    protected String description;
    protected int cost; // Cost of the item in gold (for shop)

    public Item(String name, String description, String imagePath, int cost) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.cost = cost;
    }

   
    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    // Abstraction: every item can have a use effect
    public abstract void use(Player player);

    /**
     * Indicates whether the item should be removed from inventory after a single use.
     * By default, items are not consumable. Subclasses can override this.
     */
    public boolean isConsumable() {
        return false;
    }

    /**
     * Returns the message to be displayed in the log when the item is used.
     * @param user The player who used the item.
     * @return A string describing the result of using the item.
     */
    public abstract String getUseMessage(Player user);

    /**
     * Returns a string summarizing the item's key stats for display in the UI.
     * @return An HTML-formatted string of the item's stats.
     */
    public abstract String getStatsString();
}
