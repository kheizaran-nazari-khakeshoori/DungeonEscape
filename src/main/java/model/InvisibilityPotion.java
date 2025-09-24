package model;

public class InvisibilityPotion extends Potion {

    public InvisibilityPotion(String name, String description, String imagePath) {
        // Invisibility potions have 0 heal amount and a quantity of 1.
        super(name, description, 0, 1, imagePath);
    }

    @Override
    public void use(Player player) {
        // The specific action is to apply the Invisibility effect for 2 turns.
        player.addEffect(new InvisibilityEffect(2));
    }
}