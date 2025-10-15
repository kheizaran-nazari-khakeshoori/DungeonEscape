package model;

/**
 * An effect that reduces incoming damage by half for its duration.
 * It implements IDefensiveEffect to polymorphically interact with the combat system.
 */
public class DefensiveStanceEffect implements Effect<Player>, IDefensiveEffect {
    public static final String EFFECT_NAME = "Defensive Stance";
    private int remainingDuration;

    public DefensiveStanceEffect(int duration) {
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        // This effect's primary logic is in applyDefense.
        // We just decrement its duration each turn.
        if (!isFinished()) {
            remainingDuration--;
        }
        return player.getName() + " is in a defensive stance!";
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() {
        return EFFECT_NAME;
    }

    @Override
    public int applyDefense(int incomingDamage) {
        return incomingDamage / 2; // Reduce damage by 50%
    }
}