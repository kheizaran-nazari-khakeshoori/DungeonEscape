package controller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import model.Enemy;
import model.EnemyFactory;
import model.Player;
import utils.DiceRoller;

public class DoorManager {
    private final DiceRoller dice;
    private final EnemyFactory enemyFactory;
    private final Map<String, Integer> enemyEncounterCount;
    private List<Supplier<Enemy>> currentEncounterDeck;

    public DoorManager(DiceRoller dice, EnemyFactory enemyFactory, Map<String, Integer> enemyEncounterCount) {
        this.dice = dice;
        this.enemyFactory = enemyFactory;
        this.enemyEncounterCount = enemyEncounterCount;
    }

    public void setCurrentEncounterDeck(List<Supplier<Enemy>> deck) {
        this.currentEncounterDeck = deck;
    }

    /**
     * Generates the next encounter based on dice roll.
     * @return EncounterResult containing the type and data
     */
    public EncounterResult generateEncounter(Player player, String enemyToAvoid) {
        double encounterRoll = dice.getRandom().nextDouble();
        double enemyChance = player.getRuleEngine().getRule(RuleEngine.ENEMY_CHANCE);
        if (encounterRoll < enemyChance) {
            // Enemy encounter
            if (currentEncounterDeck.isEmpty()) {
                return new EncounterResult(EncounterType.LEVEL_COMPLETE, null);
            }
            Supplier<Enemy> enemySupplier = currentEncounterDeck.remove(0);
            Enemy enemy = enemySupplier.get();
            // Avoid specific enemy if fleeing
            if (enemyToAvoid != null && enemy.getName().equals(enemyToAvoid) && !currentEncounterDeck.isEmpty()) {
                Supplier<Enemy> nextSupplier = currentEncounterDeck.remove(0);
                currentEncounterDeck.add(enemySupplier);
                enemy = nextSupplier.get();
            if (enemyToAvoid != null && enemy.getName().equals(enemyToAvoid)) {
                // Find the first different enemy in the deck to swap with
                for (int i = 0; i < currentEncounterDeck.size(); i++) {
                    if (!currentEncounterDeck.get(i).get().getName().equals(enemyToAvoid)) {
                        Supplier<Enemy> differentEnemySupplier = currentEncounterDeck.remove(i);
                        currentEncounterDeck.add(i, enemySupplier); // Put the original back
                        enemySupplier = differentEnemySupplier; // Use the new one
                        break; // Stop searching
                    }
                }
                enemy = enemySupplier.get();
            }
            // Strengthen enemy based on previous encounters
            String enemyName = enemy.getName();
            int encounterLevel = enemyEncounterCount.getOrDefault(enemyName, 0);
            if (encounterLevel > 0) {
                enemy.strengthen(encounterLevel, player.getRuleEngine());
            }
            return new EncounterResult(EncounterType.ENEMY, enemy);
        } else if (encounterRoll < enemyChance + player.getRuleEngine().getRule(RuleEngine.TRAP_CHANCE)) {
            return new EncounterResult(EncounterType.TRAP, null);
        } else {
            return new EncounterResult(EncounterType.EMPTY_ROOM, null);
        }
    }

    public void incrementEnemyCount(String enemyName) {
        int newCount = enemyEncounterCount.getOrDefault(enemyName, 0) + 1;
        enemyEncounterCount.put(enemyName, newCount);
    }

    // Inner classes for return types
    public enum EncounterType { ENEMY, TRAP, EMPTY_ROOM, LEVEL_COMPLETE }

    public record EncounterResult(EncounterType type, Enemy enemy) {}
}