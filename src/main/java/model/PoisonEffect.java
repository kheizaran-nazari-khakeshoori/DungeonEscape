package model;

public class PoisonEffect implements Effect {
    public static final String EFFECT_NAME = "Poison";
    private int damagePerTurn;
    private int remainingDuration;

    public PoisonEffect(int damagePerTurn, int duration) {
        this.damagePerTurn = damagePerTurn;
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (isFinished()) {
            return "";
        }
        player.takeDamage(damagePerTurn);
        remainingDuration--;
        return "You take " + damagePerTurn + " poison damage.";
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