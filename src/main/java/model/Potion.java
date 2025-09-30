package model;

public class Potion extends Item {
    private final int healAmount;
    private int quantity; 
    
    public Potion(String name, String description, int healAmount, int quantity, String imagePath, int cost) {
        super(name, description, imagePath, cost);
        this.healAmount = healAmount;
        this.quantity = quantity;
    }

    /**
     * This is the missing method. It allows other parts of the code
     * to ask the potion how much health it restores.
     * @return The amount of health this potion heals.
     */
    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(this.healAmount);
    }

    @Override
    public boolean isConsumable() {
        // Potions are used up after one use.
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