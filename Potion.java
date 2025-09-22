package model;

public class Potion extends Item {
    private int healingAmount;
    // The 'duration' parameter from the constructor in Main.java is unused for now,
    // but could be used for effects over time in the future.
    private int duration;

    public Potion(String name, int healingAmount, int duration) {
        super(name);
        this.healingAmount = healingAmount;
        this.duration = duration;
    }

    public int getHealingAmount() {
        return healingAmount;
    }

    /**
     * Using a Potion instantly heals the player.
     */
    @Override
    public void use(Player player) {
        player.heal(healingAmount);
    }
}