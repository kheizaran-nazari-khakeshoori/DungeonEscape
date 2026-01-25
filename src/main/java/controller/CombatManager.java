package controller;

import java.util.Iterator;

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

    public CombatManager(Player player, Iwarrior enemy, DiceRoller dice) {
        this.player = player;
        this.enemy = enemy;
        this.dice = dice;
    }

    public String performCombatRound() {
        StringBuilder combatLog = new StringBuilder();
        player.tickCooldowns();

        combatLog.append(applyEffects(player));
        combatLog.append(applyEffects(enemy));
      
        if (player.isAlive()) {
            try {
                combatLog.append(player.attack(enemy, dice));
            } catch (exceptions.InvalidMoveException e) {
                combatLog.append(player.getName()).append(" tries to attack but fails: ").append(e.getMessage());
            }
        }
       
        if (enemy.isAlive()) {
            try {
                combatLog.append("\n").append(enemy.attack(player, dice));
            } catch (exceptions.InvalidMoveException e) {
                combatLog.append("\n").append(enemy.getName()).append(" is unable to attack! (").append(e.getMessage()).append(")");
            }
        }
        return combatLog.toString();
    }

    private <T extends Iwarrior> String applyEffects(T target) {
        if (!(target instanceof IEffectable)) return "";//i am checking for compatibility not the behavior

        StringBuilder effectsLog = new StringBuilder();
        IEffectable<T> effectableTarget = (IEffectable<T>) target;

        Iterator<Effect<T>> iterator = effectableTarget.getActiveEffects().iterator();
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            effectsLog.append(effect.apply(target));
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        return effectsLog.toString();
    }
    public String useSpecialAbility() 
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
        double fleeChance = player.getRuleEngine().getRule(RuleEngine.getFleeChance());
        //This is the standard way to check for a percentage chance in programming.
        if (chance < fleeChance) 
        {
            return new FleeResult(true, "You successfully escaped!");
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
