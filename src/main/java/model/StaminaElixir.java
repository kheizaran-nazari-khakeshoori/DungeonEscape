package model;

public class StaminaElixir extends Potion {

    public StaminaElixir(String name, String description, int healAmount, String imagePath, int cost) {
        super(name, description, healAmount, 1, imagePath, cost);
    }

    @Override
    public void use(Player player) {
        super.use(player);

        player.addEffect(new Slow_Heal_Effect(5, 3));
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