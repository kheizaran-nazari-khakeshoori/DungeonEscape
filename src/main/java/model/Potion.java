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
        System.out.println("Using " + getName() + "...");
        player.heal(healing);
    }
}
