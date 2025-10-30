package model;

public class InvisibilityPotion extends Potion {

    public InvisibilityPotion(String name, String description, String imagePath, int cost) {
        super(name, description, 0, 1, imagePath, cost);
    }

    @Override
    public void use(Player player) {
        player.addEffect(new InvisibilityEffect(2));
    }

    @Override
    public String getUseMessage(Player user) {
        return "You drink the " + getName() + ". You shimmer and turn invisible! The effect will last until your next flee attempt.";
    }

    @Override
    public String getStatsString() {
        return "<b>Guarantees Flee</b>";
    }
}