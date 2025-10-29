package model;
import utils.DiceRoller;
public interface Iwarrior {
    String getName();
    int getHealth();
    int getMaxHealth();
    boolean isAlive();

   
    void takeDamage(int amount);

   
    String takeDamage(int amount, DamageType type);

    String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException;
    String applyTurnEffects();
}

