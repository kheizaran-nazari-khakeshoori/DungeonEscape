package model;

public class InvisibilityEffect implements Effect<Player> {
    public static final String EFFECT_NAME = "Invisibility";
    private int remainingDuration;

    public InvisibilityEffect(int duration) {
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        remainingDuration--;
        if (isFinished()) {
            return "Your invisibility wears off.";
        }
        return "You are invisible.";
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() { return EFFECT_NAME; }

   
}