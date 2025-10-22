package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;
//here i am using factory pattern 


public class EnemyFactory {
    private final DiceRoller dice;
    private final List<Supplier<Enemy>> enemySuppliers = new ArrayList<>();

    public EnemyFactory(DiceRoller dice) {//cunstructor dependacy injection 
        this.dice = dice;
        // Register all available enemy types here.
       
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
        // Pick a random enemy supplier from the list and create a new instance.
        int index = dice.rollIndex(enemySuppliers.size());
        return enemySuppliers.get(index).get();
    }

    /**
     * Returns a copy of the list of enemy suppliers.
     * @return A new list containing all registered enemy suppliers.
     */
    public List<Supplier<Enemy>> getEnemySuppliers() {
        return new ArrayList<>(enemySuppliers);
    }
}