package model;

public class StaminaElixir extends Potion {

    public StaminaElixir(String name, String description, int healAmount, String imagePath, int cost) {
        super(name, description, healAmount, 1, imagePath, cost);
    }

    @Override
    public void use(Player player) {
        // First, apply the parent Potion's effect (the initial heal)
        // by calling its 'use' method.
        super.use(player);

        // Add a regeneration effect: 5 health per turn for 3 turns
        player.addEffect(new ActiveEffect("Regeneration", 5, 3));
    }

    @Override
    public String getUseMessage(Player user) {
        return "You drink the " + getName() + " and feel invigorated.";
    }

    @Override
    public String getStatsString() {
        return "<b>Heals: " + getHealAmount() + " + Regen</b>";
    }
}