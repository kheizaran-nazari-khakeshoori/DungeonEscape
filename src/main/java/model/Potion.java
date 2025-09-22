package model;

public class Potion extends Item {
    private int healing;
    private int duration;

    public Potion(String name, int healing, int duration, String imagePath) {
        super(name, imagePath);
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
        Effect effect = new ActiveEffect(getName(), healing, duration);
        player.addEffect(effect);
    }

    @Override
    public boolean isConsumable() {
        return true;
    }
}
