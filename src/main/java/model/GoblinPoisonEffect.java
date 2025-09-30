package model;

/**
 * A specific, weaker poison effect applied by Goblins.
 * This demonstrates polymorphism by inclusion, as it's a unique implementation
 * of the Effect interface, but can be treated as any other Effect by the Player.
 */
public class GoblinPoisonEffect implements PoisonTypeEffect { // Implement the marker interface
    public static final String EFFECT_NAME = "Goblin Poison";
    private int damagePerTurn;
    private int remainingDuration;

    public GoblinPoisonEffect(int damagePerTurn, int duration) {
        this.damagePerTurn = damagePerTurn;
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (isFinished()) return "";

        int actualDamage = (int) (damagePerTurn * player.getPoisonResistance());
        player.takeDamage(actualDamage);
        remainingDuration--;
        return "You take " + actualDamage + " damage from the goblin's weak poison.";
    }

    @Override
    public boolean isFinished() { return remainingDuration <= 0; }

    @Override
    public String getName() { return EFFECT_NAME; }
}