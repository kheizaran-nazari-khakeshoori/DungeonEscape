package model;

import java.util.ArrayList;
import java.util.List;

public class ShopEncounter {
    private List<Item> availableItems;

    public ShopEncounter() {
        this.availableItems = new ArrayList<>();
        populateShopItems();
    }

    private void populateShopItems() {
        // Add weapons (with example costs)
        availableItems.add(new Weapon("Greatsword", "A heavy two-handed sword. High damage, but average durability.", 15, 30, DamageType.SLASHING, "images/weapons/Greatsword.png", 50));
        availableItems.add(new Weapon("Runic Bow", "A bow etched with runes. Good damage and durability.", 14, 35, DamageType.PIERCING, "images/weapons/RunicBow.png", 45));
        availableItems.add(new Weapon("Dual Daggers", "A pair of quick daggers. Lower damage, but very durable.", 8, 40, DamageType.PIERCING, "images/weapons/DualDaggers.png", 30));
        availableItems.add(new Weapon("Enchanted Staff", "A staff crackling with fire. High damage, but fragile.", 18, 20, DamageType.FIRE, "images/weapons/EnchantedStaff.png", 60));
        availableItems.add(new Weapon("War Axe", "A brutal axe that cleaves through armor. Good damage, low durability.", 16, 25, DamageType.SLASHING, "images/weapons/WarAxe.png", 40));
        availableItems.add(new Weapon("Crossbow", "A powerful crossbow that hits hard but is very fragile.", 20, 15, DamageType.PIERCING, "images/weapons/CrossBow.png", 55));

        // Add potions (with example costs)
        availableItems.add(new Potion("Health Potion", "A swirling red liquid that restores 25 health.", 25, 1, "images/potions/ManaPotion.png", 15));
        availableItems.add(new StaminaElixir("Stamina Elixir", "Restores 20 health and grants regeneration for 3 turns.", 20, "images/potions/StaminaElixir.png", 25));
        availableItems.add(new InvisibilityPotion("Invisibility Potion", "Guarantees a successful escape from your next fight.", "images/potions/InvisibilityPotion.png", 35));
        availableItems.add(new Antidote("Antidote", "A chalky fluid that cures poison.", "images/potions/Antidote.png", 20));
    }

    public List<Item> getAvailableItems() {
        return new ArrayList<>(availableItems); // Return a copy to prevent external modification
    }

    public Item getItemByName(String name) {
        return availableItems.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
    }

    public String getEnterMessage() {
        return "You enter a dimly lit shop. A shady merchant eyes you suspiciously.";
    }
}