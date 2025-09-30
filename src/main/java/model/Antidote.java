package model;

public class Antidote extends Potion {

    public Antidote(String name, String description, String imagePath, int cost) {
        super(name, description, 10, 1, imagePath, cost); // Now heals 10 HP
    }

    @Override
    public void use(Player player) {
        // The specific action for an Antidote is to remove poison.
        super.use(player); // Call the parent Potion's use() method to handle the healing.
        player.removeEffectsOfType(PoisonTypeEffect.class);
    }

    @Override
    public String getUseMessage(Player user) {
        return "You used the " + getName() + ". The poison subsides and you recover " + getHealAmount() + " health!";
    }

    @Override
    public String getStatsString() {
        return "<b>Cures Poison</b>";
    }
}