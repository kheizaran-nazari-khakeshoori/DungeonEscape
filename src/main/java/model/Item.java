package model;

public abstract class Item {
    private String name;
    protected String imagePath;
    protected String description;
    protected int cost; // Cost of the item in gold

    public Item(String name, String description, String imagePath, int cost) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.cost = cost;
    }

    // Encapsulation: getter for name
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
}
