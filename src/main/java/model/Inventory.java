package model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Item> items;

    public Inventory()
    {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    
    public Item findItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
    
    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (Item item : items)
        {
            names.add(item.getName());
        }
        return names;
    }
}


