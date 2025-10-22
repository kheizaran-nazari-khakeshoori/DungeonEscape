package model;

import utils.DiceRoller;

public interface ICombatant {
    String getName();
    int getHealth();
    int getMaxHealth();
    boolean isAlive();

    // This method is required by the ICombatant interface.
    void takeDamage(int amount);

    // Overloaded method for taking damage with a specific type
    String takeDamage(int amount, DamageType type);

    String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException;
    String applyTurnEffects();
}

