package model;

public class Slow_Heal_Effect implements Effect<Player>, IDefensiveEffect {
    public static final String EFFECT_NAME = "Slow Heal";
    private final int healPerTurn;
    private int remainingDuration;

    public Slow_Heal_Effect(int healPerTurn, int duration) {
        this.healPerTurn = healPerTurn;
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (isFinished()) return "";
        player.heal(healPerTurn);
        remainingDuration--;
        return player.getName() + " regenerates " + healPerTurn + " health.";
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() { return EFFECT_NAME; }

    @Override
    public int applyDefense(int initialDamage) { return initialDamage; } // Not a defensive effect
}
