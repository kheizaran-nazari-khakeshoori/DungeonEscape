package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;

public class EnemyFactory {
    private final DiceRoller dice;
    private final List<Supplier<Enemy>> enemySuppliers = new ArrayList<>();

    public EnemyFactory(DiceRoller dice) {
        this.dice = dice;
        
        enemySuppliers.add(Goblin::new);
        enemySuppliers.add(Ghost::new);
        enemySuppliers.add(StoneMan::new);
        enemySuppliers.add(SkeletonWarrior::new);
        enemySuppliers.add(MimicChest::new);
        enemySuppliers.add(PoisonSpider::new);
        enemySuppliers.add(ShadowAssassin::new);
        enemySuppliers.add(SlimeBlob::new);
    }

    public Enemy createRandomEnemy() {
        if (enemySuppliers.isEmpty()) {
            
            System.err.println("Warning: No enemies registered in EnemyFactory. Defaulting to Goblin.");
            return new Goblin();
        }
        
        int index = dice.rollIndex(enemySuppliers.size());
        return enemySuppliers.get(index).get();
    }                           

}