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
        this.encounters.clear();//delete the old content
        this.encounters.addAll(encounters);
    }
    public EncounterResult generateEncounter(Player player, String enemyToAvoid) {

        double randomChanceValue = dice.getRandom().nextDouble();
        double enemyChance = player.getRuleEngine().getRule(RuleEngine.getEnemyChance());

        //level complete
        if (randomChanceValue < enemyChance) 
        {
            if (encounters.isEmpty()) 
            {
                return new EncounterResult(EncounterType.LEVEL_COMPLETE, null);
            }

        Supplier<Enemy> enemySupplier = encounters.remove(0);
        Enemy enemy = enemySupplier.get();
        
        //enemy encounter
        if (enemy.getName().equals(enemyToAvoid))
        {
            //interating trough all remaining enemy suppliers in the list 
            for (int i = 0; i < encounters.size(); i++) {
                Enemy nextEnemy = encounters.get(i).get();//getting supplier enemy at index i from the ecnounters , second one calls the method of  the supplier >> so the result is enemy 
                if (!nextEnemy.getName().equals(enemyToAvoid)) {
                    //swaping with the one we want to avoid 
                    Supplier<Enemy> differentEnemySupplier = encounters.remove(i);
                    encounters.add(i, enemySupplier);
                    enemySupplier = differentEnemySupplier;
                    break;
                }
            }
            enemy = enemySupplier.get();
        }

        return new EncounterResult(EncounterType.ENEMY, enemy);
    }

    //trap encounter just determin a trap happend
    double trapChance = player.getRuleEngine().getRule(RuleEngine.getTrapChance());
    if (randomChanceValue < enemyChance + trapChance) {
        return new EncounterResult(EncounterType.TRAP, null);
    }

    // Empty room (default fallback for any remaining probability)
    return new EncounterResult(EncounterType.EMPTY_ROOM, null);
}

    public void Enemy_Appearance_count(String enemyName) {
        int newCount = enemyCount.getOrDefault(enemyName, 0) + 1;
        enemyCount.put(enemyName, newCount);
    }



   
}