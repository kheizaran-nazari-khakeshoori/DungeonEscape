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

    public void useAll(Player player) {
        for (Item item : items) {
            item.use(player); // polymorphism: different behavior for Weapon vs Potion
        }
    }
}
