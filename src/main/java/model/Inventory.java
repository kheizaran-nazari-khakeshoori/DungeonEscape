package model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public void showInventory() {
        System.out.println("Inventory:");
        for (Item item : items) {
            System.out.println("- " + item.getName());
        }
    }

    // Return a copy of the items list for safe access
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    // Get first weapon in inventory
    public Weapon getFirstWeapon() {
        for (Item item : items) {
            if (item instanceof Weapon) {
                return (Weapon) item;
            }
        }
        return null;
    }
}
