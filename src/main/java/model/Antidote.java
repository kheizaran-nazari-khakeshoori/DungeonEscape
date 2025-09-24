package model;

public class Antidote extends Potion {

    public Antidote(String name, String description, String imagePath) {
        // Antidotes have 0 heal amount and a quantity of 1.
        super(name, description, 0, 1, imagePath);
    }

    @Override
    public void use(Player player) {
        // The specific action for an Antidote is to remove poison.
        player.removeEffect(PoisonEffect.EFFECT_NAME);
    }
}