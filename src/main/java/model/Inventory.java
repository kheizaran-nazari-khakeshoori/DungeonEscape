package model;

import java.util.ArrayList;
import java.util.List;
//one responsiblity >> manage collection of item 
public class Inventory {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    // Return a copy of the items list for safe access
    public List<Item> getItems() {//get all item 
        return new ArrayList<>(items);
    }

    // Find an item by name >> check if it need to be deleted 
    public Item findItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
    
    public List<String> getItemNames() {
        return items.stream()
                    .map(Item::getName)
                    .toList();
    }
}

//the two type of polymorphism must be considered for this line >> private List<Item> items = new ArrayList<>();
