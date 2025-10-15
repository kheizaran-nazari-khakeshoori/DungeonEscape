package model;

/**
 * An interface for effects that can modify incoming damage.
 * This allows for a polymorphic way to handle various defensive effects
 * without the Player class needing to know about specific effect types.
 */
public interface IDefensiveEffect {
    /**
     * Applies the defensive modification to incoming damage.
     * @param incomingDamage The initial amount of damage.
     * @return The modified damage amount after the effect is applied.
     */
    int applyDefense(int incomingDamage);
}