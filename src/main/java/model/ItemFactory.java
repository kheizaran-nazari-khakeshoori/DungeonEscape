package model;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {
    
    public static List<Weapon> createStartingWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(new Weapon("Greatsword", "A heavy two-handed sword. High damage, but average durability.", 
                               15, 30, DamageType.SLASHING, "images/weapons/Greatsword.png", 0));
        weapons.add(new Weapon("Runic Bow", "A bow etched with runes. Good damage and durability.", 
                               14, 35, DamageType.PIERCING, "images/weapons/RunicBow.png", 0));
        weapons.add(new Weapon("Dual Daggers", "A pair of quick daggers. Lower damage, but very durable.", 
                               8, 40, DamageType.PIERCING, "images/weapons/DualDaggers.png", 0));
        weapons.add(new Weapon("Enchanted Staff", "A staff crackling with fire. High damage, but fragile.", 
                               18, 20, DamageType.FIRE, "images/weapons/EnchantedStaff.png", 0));
        weapons.add(new Weapon("War Axe", "A brutal axe that cleaves through armor. Good damage, low durability.", 
                               16, 25, DamageType.SLASHING, "images/weapons/WarAxe.png", 0));
        weapons.add(new Weapon("Crossbow", "A powerful crossbow that hits hard but is very fragile.", 
                               20, 15, DamageType.PIERCING, "images/weapons/CrossBow.png", 0));
        return weapons;
    }
    
    public static List<Potion> createStartingPotions() {
        List<Potion> potions = new ArrayList<>();
        potions.add(new Potion("Health Potion", "A swirling blue liquid that restores 25 health.", 
                               25, 1, "images/potions/ManaPotion.png", 0));
        potions.add(new StaminaElixir("Stamina Elixir", "Restores 20 health and grants regeneration for 3 turns.", 
                                      20, "images/potions/StaminaElixir.png", 0));
        potions.add(new InvisibilityPotion("Invisibility Potion", "Guarantees a successful escape from your next fight.", 
                                           "images/potions/InvisibilityPotion.png", 0));
        potions.add(new Antidote("Antidote", "A chalky fluid that cures poison.", 
                                 "images/potions/Antidote.png", 0));
        return potions;
    }
}