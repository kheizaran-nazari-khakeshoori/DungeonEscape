package model;
import java.util.ArrayList;
import java.util.List;

//manages shop access 

public class ShopEncounter {
    private final List<Item> availableItems; // List depends on abstract class

   
    public ShopEncounter(List<Item> items) {
        // Defensive copy to prevent external modification
        this.availableItems = new ArrayList<>(items);
    }

    public List<Item> getAvailableItems() {
        return new ArrayList<>(availableItems); 
    }

   public Item getItemByName(String name) {

        for (Item item : availableItems) {
           
            if (item.getName().equals(name)) {
               
                return item;
            }
        }
        return null;
    }


    public String getEnterMessage() {
        return "You enter the shop.";
    }
}
//for the future i can add selling logic , discount logic 