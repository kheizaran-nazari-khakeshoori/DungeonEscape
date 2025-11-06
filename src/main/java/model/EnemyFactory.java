package model;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;
//factory pattern that create random enemy instance (i centralized the enemy creation)
public class EnemyFactory {
    private final DiceRoller dice;
    private final List<Supplier<Enemy>> enemySuppliers ;

    public EnemyFactory(DiceRoller dice) {
        this.dice = dice;
        this.enemySuppliers = new ArrayList<>();
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
        
        int index = dice.rollIndex(enemySuppliers.size());//returning random index
        return enemySuppliers.get(index).get();
    }                        //get from list  get from supplier 

}