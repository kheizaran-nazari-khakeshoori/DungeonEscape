package model;

public class InvisibilityEffect implements Effect {
    public static final String EFFECT_NAME = "Invisibility";
    private int remainingDuration;

    public InvisibilityEffect(int duration) {
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        // This effect is passive. It's checked by other game mechanics.
        // We just decrement its duration each turn.
        if (!isFinished()) {
            remainingDuration--;
        }
        return ""; // Passive effects don't need to log anything each turn
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() {
        return EFFECT_NAME;
    }
}