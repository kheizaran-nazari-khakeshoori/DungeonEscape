package model;

/**
 * An effect that reduces incoming damage for a short duration.
 */
public class DefensiveStanceEffect implements Effect<Player> {
    public static final String EFFECT_NAME = "Defensive Stance";
    private int turnsRemaining;

    public DefensiveStanceEffect(int duration) {
        this.turnsRemaining = duration;
    }

    @Override
    public String apply(Player target) {
        // This effect doesn't do anything on its own turn, it's a passive buff.
        // We could add a message here if we wanted.
        turnsRemaining--;
        return null; // No message needed each turn.
    }

    @Override
    public boolean isFinished() {
        return turnsRemaining <= 0;
    }

    @Override
    public String getName() {
        return EFFECT_NAME;
    }
}
