package model;

public class Potion extends Item {
    private final int healAmount;

    public Potion(String name, String description, int healAmount, int quantity, String imagePath, int cost) {
        super(name, description, imagePath, cost);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(this.healAmount);
    }

    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public String getUseMessage(Player user) {
        return "Used " + getName() + ".";
    }

    @Override
    public String getStatsString() {
        return "<b>Heals: " + getHealAmount() + " HP</b>";
    }
}