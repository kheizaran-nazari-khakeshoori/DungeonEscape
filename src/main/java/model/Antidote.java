package model;

public class Antidote extends Potion {

    public Antidote(String name, String description, String imagePath, int cost) {
        super(name, description, 0, 1, imagePath, cost);
    }

    @Override
    public void use(Player player) {
        // The specific action for an Antidote is to remove poison.
        player.removeEffect(PoisonEffect.EFFECT_NAME);
    }

    @Override
    public String getUseMessage(Player user) {
        return "You used the " + getName() + ". The poison subsides!";
    }

    @Override
    public String getStatsString() {
        return "<b>Cures Poison</b>";
    }
}