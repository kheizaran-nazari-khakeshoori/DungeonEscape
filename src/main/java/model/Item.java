package model;

public abstract class Item {
    private String name;
    protected String imagePath;
    protected String description;

    public Item(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
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
