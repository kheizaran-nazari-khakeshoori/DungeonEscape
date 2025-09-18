package model;

public class Player {
    private String name;
    private int health;
    private Inventory inventory;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.inventory = new Inventory();
    }

    public void pickItem(Item item) {
        inventory.addItem(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    public Inventory getInventory() {
        return inventory;
    }

    // <--- ADD THIS
    public String getName() {
        return name;
    }
}
