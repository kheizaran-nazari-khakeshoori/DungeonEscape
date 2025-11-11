package controller;

import exceptions.InvalidMoveException;
import model.Enemy;
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
            } catch (exceptions.InvalidMoveException e) {}
        }

        
        String playerEffects = player.applyTurnEffects();
        if (!playerEffects.isEmpty()) {
            combatLog.append("\n").append(playerEffects);
        }

        String enemyEffects = enemy.applyTurnEffects();
        if (!enemyEffects.isEmpty()) {
            combatLog.append("\n").append(enemyEffects);
        }

        return combatLog.toString();
    }

    public String useSpecialAbility() 
    {
        if (!player.isSpecialAbilityReady()) 
        {
            return player.getName() + "'s special ability is not ready!";
        }
        
        if (!(enemy instanceof Enemy)) 
        {
       
            return player.getName() + " can't use their ability on this target.";
        } 
        else 
        {
            Enemy realEnemy = (Enemy) enemy; 
            return player.useSpecialAbility(realEnemy);
        }
    }

    

    public FleeResult attemptFlee() {
        
        if (player.hasEffect(InvisibilityEffect.EFFECT_NAME)) {
           
            player.removeEffect(InvisibilityEffect.EFFECT_NAME);
            return new FleeResult(true, "Your invisibility allows you to slip away unnoticed!");
        }  
        
        double chance = dice.getRandom().nextDouble();
        double fleeChance = player.getRuleEngine().getRule(RuleEngine.FLEE_CHANCE);

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
            }       
            catch (InvalidMoveException e) 
            {
            return new FleeResult(false, "You failed to escape!");
        }
        }
    }

    public record FleeResult(boolean success, String logMessage) {}
}