package model;

public class PoisonEffect implements PoisonTypeEffect {
    public static final String EFFECT_NAME = "Poison";
    private final int damagePerTurn;
    private int remainingDuration;

    public PoisonEffect(int damagePerTurn, int duration) {
        this.damagePerTurn = damagePerTurn;
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (isFinished()) return "";

        int actualDamage = (int) (damagePerTurn * player.getPoisonResistance());
        player.takeDamage(actualDamage);
        remainingDuration--;
        return player.getName() + " takes " + actualDamage + " damage from poison.";
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() { return EFFECT_NAME; }

}