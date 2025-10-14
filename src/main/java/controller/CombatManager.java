package controller;

import model.Enemy;
import model.ICombatant;
import model.Player;
import utils.DiceRoller;

public class CombatManager {
    private final Player player;
    private final ICombatant enemy;
    private final DiceRoller dice;

    public CombatManager(Player player, ICombatant enemy, DiceRoller dice) {
        this.player = player;
        this.enemy = enemy;
        this.dice = dice;
    }

    public String performCombatRound() {
        StringBuilder combatLog = new StringBuilder();
        player.tickCooldowns();

        // Player attacks enemy
        if (player.isAlive()) {
            try {
                combatLog.append(player.attack(enemy, dice));
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

    public String useSpecialAbility() {
        if (!player.isSpecialAbilityReady()) {
            return player.getName() + "'s special ability is not ready!";
        }
        if (enemy instanceof Enemy) {
            return player.useSpecialAbility((Enemy) enemy, dice);
        }
        return player.getName() + " can't use their ability on this target.";
    }

    public FleeResult attemptFlee() {
        if (dice.getRandom().nextDouble() < player.getRuleEngine().getRule(RuleEngine.FLEE_CHANCE)) {
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