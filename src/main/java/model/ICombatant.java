package model;

import utils.DiceRoller;

public interface ICombatant {
    String getName();
    int getHealth();
    int getMaxHealth();
    boolean isAlive();
    void takeDamage(int amount);
    String applyTurnEffects();
    String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException;
}