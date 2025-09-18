package model;

public class Potion extends Item {
    private int healing;
    private int duration;

    public Potion(String name, int healing, int duration) {
        super(name);
        this.healing = healing;
        this.duration = duration;
    }

    public int getHealing() {
        return healing;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " heals " + player.getName() + " for " + healing + " health for " + duration + " turns!");
        // Optionally increase player health
        // player.heal(healing);
    }
}
