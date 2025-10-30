package controller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import model.Enemy;
import model.Player;
import utils.DiceRoller;

public class DoorManager {
    private final DiceRoller dice;
    private final Map<String, Integer> enemyCount;
    private List<Supplier<Enemy>> encounters;

    public DoorManager(DiceRoller dice, Map<String, Integer> enemyCount) {
        this.dice = dice;

        this.enemyCount = enemyCount;
    }

    public void setencounters(List<Supplier<Enemy>> enemySuppliers) {
        this.encounters = enemySuppliers;
    }

 
    public EncounterResult generateEncounter(Player player, String enemyToAvoid) {

        double encounterRoll = dice.getRandom().nextDouble();
        double enemyChance = player.getRuleEngine().getRule(RuleEngine.ENEMY_CHANCE);

   
        if (encounterRoll < enemyChance) 
        {
            if (encounters.isEmpty()) 
            {
                return new EncounterResult(EncounterType.LEVEL_COMPLETE, null);
            }

        Supplier<Enemy> enemySupplier = encounters.remove(0);
        Enemy enemy = enemySupplier.get();

        if (enemyToAvoid != null && enemy.getName().equals(enemyToAvoid))
        {
            for (int i = 0; i < encounters.size(); i++) {
                Enemy nextEnemy = encounters.get(i).get();
                if (!nextEnemy.getName().equals(enemyToAvoid)) {
                    
                    Supplier<Enemy> differentEnemySupplier = encounters.remove(i);
                    encounters.add(i, enemySupplier); 
                    enemySupplier = differentEnemySupplier; 
                    break;
                }
            }
            enemy = enemySupplier.get();
        }

        String enemyName = enemy.getName();
        int previousEncounters = enemyCount.getOrDefault(enemyName, 0);
        if (previousEncounters > 0) {
            enemy.strengthen(previousEncounters, player.getRuleEngine());
        }

        return new EncounterResult(EncounterType.ENEMY, enemy);
    }

    double trapChance = player.getRuleEngine().getRule(RuleEngine.TRAP_CHANCE);
    if (encounterRoll < enemyChance + trapChance) {
        return new EncounterResult(EncounterType.TRAP, null);
    }

    return new EncounterResult(EncounterType.EMPTY_ROOM, null);
}


    public void Enemy_Appearance_count(String enemyName) {
        int newCount = enemyCount.getOrDefault(enemyName, 0) + 1;
        enemyCount.put(enemyName, newCount);
    }

    public enum EncounterType { ENEMY, TRAP, EMPTY_ROOM, LEVEL_COMPLETE }

    public record EncounterResult(EncounterType type, Enemy enemy) {}
}