package model;
// This class manages shop inventory and provides access to items
// Uses dependency injection to follow Open/Closed Principle
import java.util.ArrayList;
import java.util.List;

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
// REFACTORED DESIGN BENEFITS:
// 1. Open/Closed Principle: Open for extension (create different shops), closed for modification
// 2. Single Responsibility: This class only manages shop access, not item creation
// 3. Dependency Injection: Items are injected via constructor for flexibility and testability
// 4. No Code Duplication: Item creation centralized in ItemFactory
// 5. Examples of different shop types:
//    - new ShopEncounter(ItemFactory.createShopItems())       // Standard shop
//    - new ShopEncounter(createPotionOnlyShop())              // Potion specialty shop
//    - new ShopEncounter(createBeginnerShop())                // Beginner-friendly shop
//    - new ShopEncounter(loadShopFromFile("advanced.json"))   // Data-driven shops