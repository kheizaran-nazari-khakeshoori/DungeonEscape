package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import model.Enemy;
import model.Player;
import utils.DiceRoller;
//this class is for managing the encounters when a player goes through a door

public class DoorManager {
    private final DiceRoller dice;
    private final Map<String, Integer> enemyCount;
    private final List<Supplier<Enemy>> encounters;

    public DoorManager(DiceRoller dice, Map<String, Integer> enemyCount) {
        this.dice = dice;
        this.encounters = new ArrayList<>();
        this.enemyCount = enemyCount;
    }

    public void setEncounters(List<Supplier<Enemy>> encounters) {
        this.encounters.clear();
        this.encounters.addAll(encounters);
    }
    public EncounterResult generateEncounter(Player player, String enemyToAvoid) {

        double randomChanceValue = dice.getRandom().nextDouble();
        double enemyChance = player.getRuleEngine().getRule(RuleEngine.ENEMY_CHANCE);

   
        if (randomChanceValue < enemyChance) 
        {
            if (encounters.isEmpty()) 
            {
                return new EncounterResult(EncounterType.LEVEL_COMPLETE, null);
            }

        Supplier<Enemy> enemySupplier = encounters.remove(0);
        Enemy enemy = enemySupplier.get();

        if (enemy.getName().equals(enemyToAvoid))
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
        int previousEncounters = enemyCount.getOrDefault(enemyName, 0);//return the value if the key exists, otherwise return 0
        if (previousEncounters > 0) {
            enemy.strengthen(previousEncounters, player.getRuleEngine());
        }

        return new EncounterResult(EncounterType.ENEMY, enemy);
    }

    double trapChance = player.getRuleEngine().getRule(RuleEngine.TRAP_CHANCE);
    if (randomChanceValue < enemyChance + trapChance) {
        return new EncounterResult(EncounterType.TRAP, null);
    }

    return new EncounterResult(EncounterType.EMPTY_ROOM, null);
}


    public void Enemy_Appearance_count(String enemyName) {
        int newCount = enemyCount.getOrDefault(enemyName, 0) + 1;
        enemyCount.put(enemyName, newCount);
    }



    public record EncounterResult(EncounterType type, Enemy enemy) {}
}