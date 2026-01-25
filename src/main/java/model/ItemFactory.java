package model;

import java.util.ArrayList;
import java.util.List;
// this is an utility class with static methods , i do not want anyone to extend this class 
//Why static?
//No state needed - ItemFactory doesn't store any data, so no need for instances
//Convenience - Call directly: ItemFactory.createStartingWeapons() instead of new ItemFactory().createStartingWeapons()
//Utility class pattern - Pure object creation logic
public final class ItemFactory {
    
    // ========== WEAPON CREATION HELPERS (DRY - Define once, reuse everywhere) ==========
    
    private static Weapon createGreatsword(int cost) {
        return new Weapon("Greatsword", "A heavy two-handed sword. High damage, but average durability.", 
                         15, 30, DamageType.SLASHING, "images/weapons/Greatsword.png", cost);
    }
    
    private static Weapon createRunicBow(int cost) {
        return new Weapon("Runic Bow", "A bow etched with runes. Good damage and durability.", 
                         14, 35, DamageType.PIERCING, "images/weapons/RunicBow.png", cost);
    }
    
    private static Weapon createDualDaggers(int cost) {
        return new Weapon("Dual Daggers", "A pair of quick daggers. Lower damage, but very durable.", 
                         8, 40, DamageType.PIERCING, "images/weapons/DualDaggers.png", cost);
    }
    
    private static Weapon createEnchantedStaff(int cost) {
        return new Weapon("Enchanted Staff", "A staff crackling with fire. High damage, but fragile.", 
                         18, 20, DamageType.FIRE, "images/weapons/EnchantedStaff.png", cost);
    }
    
    private static Weapon createWarAxe(int cost) {
        return new Weapon("War Axe", "A brutal axe that cleaves through armor. Good damage, low durability.", 
                         16, 25, DamageType.SLASHING, "images/weapons/WarAxe.png", cost);
    }
    
    private static Weapon createCrossbow(int cost) {
        return new Weapon("Crossbow", "A powerful crossbow that hits hard but is very fragile.", 
                         20, 15, DamageType.PIERCING, "images/weapons/CrossBow.png", cost);
    }
    
    // ========== POTION CREATION HELPERS (DRY - Define once, reuse everywhere) ==========
    
    private static Potion createHealthPotion(int cost) {
        return new Potion("Health Potion", "A swirling blue liquid that restores 25 health.", 
                         25, 1, "images/potions/ManaPotion.png", cost);
    }
    
    private static StaminaElixir createStaminaElixir(int cost) {
        return new StaminaElixir("Stamina Elixir", "Restores 20 health and grants regeneration for 3 turns.", 
                                20, "images/potions/StaminaElixir.png", cost);
    }
    
    private static InvisibilityPotion createInvisibilityPotion(int cost) {
        return new InvisibilityPotion("Invisibility Potion", "Guarantees a successful escape from your next fight.", 
                                     "images/potions/InvisibilityPotion.png", cost);
    }
    
    private static Antidote createAntidote(int cost) {
        return new Antidote("Antidote", "A chalky fluid that cures poison.", 
                           "images/potions/Antidote.png", cost);
    }
    
    // ========== PUBLIC API - Starting Items (cost = 0) ==========
    
    public static List<Weapon> createStartingWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(createGreatsword(0));
        weapons.add(createRunicBow(0));
        weapons.add(createDualDaggers(0));
        weapons.add(createEnchantedStaff(0));
        weapons.add(createWarAxe(0));
        weapons.add(createCrossbow(0));
        return weapons;
    }
    
    public static List<Potion> createStartingPotions() {
        List<Potion> potions = new ArrayList<>();
        potions.add(createHealthPotion(0));
        potions.add(createStaminaElixir(0));
        potions.add(createInvisibilityPotion(0));
        potions.add(createAntidote(0));
        return potions;
    }
    
    // ========== PUBLIC API - Shop Items (with prices) ==========
    
    public static List<Item> createShopItems() {
        List<Item> items = new ArrayList<>();
        
        // Add weapons with shop prices
        items.add(createGreatsword(50));
        items.add(createRunicBow(45));
        items.add(createDualDaggers(30));
        items.add(createEnchantedStaff(60));
        items.add(createWarAxe(40));
        items.add(createCrossbow(55));
        
        // Add potions with shop prices
        items.add(createHealthPotion(15));
        items.add(createStaminaElixir(25));
        items.add(createInvisibilityPotion(35));
        items.add(createAntidote(20));
        
        return items;
    }
}

// ========== OOP PRINCIPLES & DESIGN PATTERNS DEMONSTRATED ==========
//
// 1. DRY (Don't Repeat Yourself) ✅
//    - Each item defined ONCE in private helper methods
//    - Helper methods reused with different costs
//    - Single source of truth: change Greatsword stats in ONE place
//
// 2. Encapsulation ✅
//    - Private helper methods hide implementation details
//    - Public API exposes only what clients need
//    - Internal item creation logic is encapsulated
//
// 3. Single Responsibility Principle ✅
//    - ItemFactory has ONE job: create items
//    - Each helper method creates ONE specific item type
//
// 4. Open/Closed Principle (Improved) ⚠️
//    - Still requires modification to add new items (acceptable for static factory)
//    - But changes are localized to helper methods
//    - Easy to add new shop types: createBeginnnerShopItems(), createAdvancedShopItems()
//
// 5. Factory Pattern ✅
//    - Centralizes object creation logic
//    - Clients don't need to know item construction details
//    - Consistent item creation across the application
//
// 6. Parameterization ✅
//    - Helper methods use cost parameter for flexibility
//    - Same item definition works for starting items (cost=0) and shop items (cost>0)
//
// 7. Code Maintainability ✅
//    - To change Greatsword damage: edit ONE method (createGreatsword)
//    - To add new shop type: create new public method using existing helpers
//    - To add new item: create helper + update relevant public methods
//
// BENEFITS OF THIS REFACTORED DESIGN:
// • No code duplication - each item defined once
// • Easy to maintain - change stats in one place
// • Flexible - can create items with any cost
// • Extensible - easy to add new item collections
// • Testable - can test individual item creation
// • Clear separation between definition (helpers) and composition (public methods)