package controller;

import model.Enemy;
import model.Iwarrior;
import model.Player;
import utils.DiceRoller;

public class CombatManager {
    private final Player player;
    private final Iwarrior enemy;
    private final DiceRoller dice;

    public CombatManager(Player player, Iwarrior enemy, DiceRoller dice) {
        this.player = player;
        this.enemy = enemy;
        this.dice = dice;
    }

    public String performCombatRound() {//why do you alway  forget the method type is string 
        StringBuilder combatLog = new StringBuilder();// the append method modifies the same object instead of creating new object >> at the end i concert it into the string
        player.tickCooldowns();//delegating to the player class 

        // Player attacks enemy
        if (player.isAlive()) {
            try {
                combatLog.append(player.attack(enemy, dice));//polymorphism 
            } catch (exceptions.InvalidMoveException e) {
                combatLog.append(player.getName()).append(" tries to attack but fails: ").append(e.getMessage());
            }
        }

        // Enemy attacks player (if it's an Enemy)
        if (enemy.isAlive()) {
            try {
                combatLog.append("\n").append(enemy.attack(player, dice));
            } catch (exceptions.InvalidMoveException e) { /* Enemy attack shouldn't fail this way */ }
        }

        // Apply effects
        String playerEffects = player.applyTurnEffects();//effect message 
        if (!playerEffects.isEmpty()) {//for preventing the empty lines in combat log 
            combatLog.append("\n").append(playerEffects);
        }
        String enemyEffects = enemy.applyTurnEffects();
        if (!enemyEffects.isEmpty()) {
            combatLog.append("\n").append(enemyEffects);
        }

        return combatLog.toString();
    }

    public String useSpecialAbility() {
        if (!player.isSpecialAbilityReady()) {
            return player.getName() + "'s special ability is not ready!";
        }
        if (enemy instanceof Enemy enemy1) {
            return player.useSpecialAbility(enemy1);   
        }
        return player.getName() + " can't use their ability on this target.";
    }

    public FleeResult attemptFlee() {
        // --- IMPROVEMENT: Check for invisibility before rolling for a normal flee ---
        if (player.hasEffect("Invisibility")) {
            // Consume the effect for a guaranteed escape
            player.removeEffect("Invisibility");
            return new FleeResult(true, "Your invisibility allows you to slip away unnoticed!");
        }

        if (dice.getRandom().nextDouble() < player.getRuleEngine().getRule(RuleEngine.FLEE_CHANCE)) { // Normal flee chance
            return new FleeResult(true, "You successfully escaped!");
        } else {
            int damageTaken = 15; // Penalty for failing to flee
            player.takeDamage(damageTaken);
            String message = "You failed to escape and " + enemy.getName() + " gets a free hit! You take " + damageTaken + " damage.";
            return new FleeResult(false, message);
        }
    }

    public record FleeResult(boolean success, String logMessage) {}
}