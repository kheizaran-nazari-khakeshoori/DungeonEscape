package controller;

import java.util.Iterator;

import exceptions.InvalidMoveException;
import model.Effect;
import model.IEffectable;
import model.InvisibilityEffect;
import model.Iwarrior;
import model.Player;
import utils.DiceRoller;
//Controller class that coordinates combat interactions between Player and Iwarrior implementations
public class CombatManager {
    private final Player player;
    private final Iwarrior enemy;
    private final DiceRoller dice;

    public CombatManager(Player player, Iwarrior enemy, DiceRoller dice, int previousEncounters) {
        this.player = player;
        this.enemy = enemy;
        this.dice = dice;
        
        // Strengthen enemy based on previous encounters with this enemy type
        if (previousEncounters > 0 && enemy instanceof model.Enemy) {
            ((model.Enemy) enemy).strengthen(previousEncounters, player.getRuleEngine());
        }
    }

    public String performCombatRound() {
        StringBuilder combatLog = new StringBuilder();
        player.tickCooldowns();

        combatLog.append(applyEffects(player));// ← Player IS-A Iwarrior
        combatLog.append(applyEffects(enemy)); // ← Iwarrior
      
        if (player.isAlive()) {
            try {
                combatLog.append(player.attack(enemy, dice));
            } catch (exceptions.InvalidMoveException e) {
                combatLog.append(player.getName()).append(" tries to attack but fails: ").append(e.getMessage());
            }
        }
       
        if (enemy.isAlive()) {
            try {
                String attackMsg = enemy.attack(player, dice);
                combatLog.append("\n").append(attackMsg);
            } catch (exceptions.InvalidMoveException e) {
                combatLog.append("\n").append(enemy.getName()).append(" is unable to attack! (").append(e.getMessage()).append(")");
            }
        }
        return combatLog.toString();
    }

    private <T extends Iwarrior> String applyEffects(T target) {
        if (!(target instanceof IEffectable)) return "";//i am checking for compatibility not the behavior , instanceof prevent crash 
    

        StringBuilder effectsLog = new StringBuilder();
        IEffectable<T> effectableTarget = (IEffectable<T>) target; // ← Subtyping! Same object, different type

        Iterator<Effect<T>> iterator = effectableTarget.getActiveEffects().iterator();
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            effectsLog.append(effect.apply(target)); // ← Both references used on same object
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        return effectsLog.toString();
    }
    public String useSpecialAbility() throws InvalidMoveException
    {
        if (!player.isSpecialAbilityReady()) 
        {
            return player.getName() + "'s special ability is not ready!";
        }
        
        return player.useSpecialAbility(enemy);
    }

    
    // Attempts to flee from combat. If the player has the Invisibility effect, fleeing always succeeds and the effect is removed.
    // Otherwise, a random chance is compared to the player's flee chance rule. If the flee fails, the enemy gets a free attack.
    // Returns a FleeResult indicating whether the escape was successful and a message describing the outcome.

    public FleeResult attemptFlee() {
        
        if (player.hasEffect(InvisibilityEffect.EFFECT_NAME)) {
           
            player.removeEffect(InvisibilityEffect.EFFECT_NAME);
            return new FleeResult(true, "Your invisibility allows you to slip away unnoticed!");
        }  
        
        double chance = dice.getRandom().nextDouble();
        //This is a chained method call:
        //dice.getRandom() - Calls the getRandom() method on the DiceRoller object, which returns Java's Random object (the random number generator)
        //.nextDouble() - Calls nextDouble() on that Random object, which generates a random decimal number between 0.0 (inclusive) and 1.0 (exclusive)

        double fleeChance = player.getRuleEngine().getRule(RuleEngine.getFleeChance());
        //This is the standard way to check for a percentage chance in programming.
        if (chance < fleeChance) 
        {
            return new FleeResult(true, "You successfully escaped!");// creating fleeresult object (composition)
        } 
        else
        {
            try {
                String attackResult = enemy.attack(player, dice);
                String message = "You failed to escape!\n" + attackResult;
                return new FleeResult(false, message);
            } catch (exceptions.InvalidMoveException e) {
                return new FleeResult(false, "You failed to escape!");
            }
        }
    }

    
}


/**
 * Type Safety vs. Polymorphism Design Decision:
 * 
 * This class uses Player and Iwarrior base types rather than generic parameters
 * (e.g., CombatManager<P extends Player, E extends Iwarrior>) to preserve runtime
 * polymorphism. 
 * 
 * With 3 player types (Bean, Lucy, Elfo) and 8 enemy types, the game requires
 * dynamic type selection at runtime. Making this class generic would require
 * knowing exact types at compile time (e.g., CombatManager<Bean, Goblin>),
 * which conflicts with polymorphic enemy/player selection.
 * 
 * Trade-off Analysis:
 * - Generic approach: Perfect compile-time type safety, but loses runtime flexibility
 * - Current approach: Full runtime polymorphism, type safety through polymorphic dispatch
 * 
 * The current design follows the Dependency Inversion Principle (depend on abstractions)
 * and enables the game to create combat between any Player and Iwarrior combination
 * without compile-time type constraints.
 * 
 * Note: Due to type erasure, the generic approach wouldn't provide runtime benefits
 * anyway, while the current approach maintains clean, flexible OOP design.
 */
//"I used Iwarrior interface to follow the Dependency Inversion Principle. This allows maximum flexibility - any class implementing Iwarrior can be used as an enemy. Due to Java's type erasure, the generic type information is lost at runtime anyway, so using a more specific type wouldn't provide additional type safety. The code works correctly and follows interface-based design principles."


// for double randomchance 
// In DiceRoller.java
// public double nextDouble() {
//     return random.nextDouble();
// }

// // In CombatManager.java
// double chance = dice.nextDouble(); // ✓ Only talks to dice, not dice's internals