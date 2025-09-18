package model;

public abstract class Item {
    private String name;

    public Item(String name) {
        this.name = name;
    }

    // Encapsulation: getter for name
    public String getName() {
        return name;
    }

    // Abstraction: every item can have a use effect
    public abstract void use(Player player);
}
