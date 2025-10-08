package controller;

import exceptions.InvalidMoveException;
import model.Enemy;
import model.InvisibilityEffect;
import model.Player;
import utils.DiceRoller;

/**
 * Manages the state and logic of a single combat encounter.
 * This class has a single responsibility: to handle a fight between a player and an enemy.
 */
public class CombatManager {

    private final Player player;
    private final Enemy enemy;
    private final DiceRoller dice;
    private final RuleEngine ruleEngine;

    public CombatManager(Player player, Enemy enemy, DiceRoller dice) {
        this.player = player;
        this.enemy = enemy;
        this.dice = dice;
        this.ruleEngine = player.getRuleEngine(); // Use the player's rules for this combat
    }

    /**
     * Executes a full round of combat: player effects, player attack, and then enemy attack.
     * @return A log of the combat round events.
     */
    public String performCombatRound() {
        StringBuilder log = new StringBuilder();

        // --- Player's Turn ---
        player.tickCooldowns();

        String effectsResult = player.applyTurnEffects();
        if (!effectsResult.isEmpty()) {
            log.append(effectsResult).append("\n");
        }

        // Check if player is still alive after effects like poison
        if (!player.isAlive()) {
            return log.toString();
        }

        try {
            String playerAttackResult = player.attack(enemy, dice);
            log.append(playerAttackResult);
        } catch (InvalidMoveException e) {
            log.append("Cannot attack: ").append(e.getMessage());
        }

        // Check if enemy is defeated
        if (!enemy.isAlive()) {
            return log.toString();
        }

        // --- Enemy's Turn ---
        log.append("\n").append(enemy.attack(player, dice));

        return log.toString();
    }

    /**
     * Handles the player's attempt to use a special ability.
     * @return A log of the ability's result and the enemy's subsequent turn if applicable.
     */
    public String useSpecialAbility() {
        if (!player.isSpecialAbilityReady()) {
            return "Your special ability is not ready yet! (" + player.getCurrentSpecialAbilityCooldown() + " turns remaining)";
        }

        String abilityResult = player.useSpecialAbility(enemy, dice);

        // If the fight continues after the ability, the enemy gets its turn.
        if (player.isAlive() && enemy.isAlive()) {
            abilityResult += "\n" + enemy.attack(player, dice);
        }

        return abilityResult;
    }

    /**
     * Handles the player's attempt to flee.
     * @return A result object indicating if the flee was successful.
     */
    public FleeResult attemptFlee() {
        boolean isGuaranteed = player.hasEffect(InvisibilityEffect.EFFECT_NAME);
        if (isGuaranteed || dice.getRandom().nextDouble() < ruleEngine.getRule(RuleEngine.FLEE_CHANCE)) {
            if (isGuaranteed) {
                player.removeEffect(InvisibilityEffect.EFFECT_NAME);
                return new FleeResult(true, "You vanish from sight and easily escape!");
            }
            return new FleeResult(true, "You successfully flee from the battle and rush through another door...");
        } else {
            // Fleeing fails, so the enemy gets a free attack.
            String enemyAttackLog = "You try to flee, but the " + enemy.getName() + " blocks your path!\n";
            enemyAttackLog += enemy.attack(player, dice);
            return new FleeResult(false, enemyAttackLog);
        }
    }

    /**
     * A simple data class to hold the result of a flee attempt.
     */
    public static class FleeResult {
        public final boolean success;
        public final String logMessage;

        public FleeResult(boolean success, String logMessage) {
            this.success = success;
            this.logMessage = logMessage;
        }
    }
}
