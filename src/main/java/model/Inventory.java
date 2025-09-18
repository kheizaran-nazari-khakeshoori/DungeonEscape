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

    public Weapon getFirstWeapon() {
    for (Item item : items) {
        if (item instanceof Weapon) {
            return (Weapon) item; // cast Item → Weapon
        }
    }
    return null;
}

}
