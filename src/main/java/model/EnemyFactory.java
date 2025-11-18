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
//Supplier avoids bugs that could arise from reusing the same object instance, such as health or status effects carrying over between different encounters."
//"I used Supplier throughout my game wherever I needed to generate new instances of objects (such as enemies or encounters) on demand.
//While my EnemyFactory does require modification when adding new enemy types, this approach keeps the code simple, clear, and easy to maintain for a game of this size. It centralizes all enemy creation logic, making it straightforward to see and control which enemies are available. More advanced Open/Closed Principle solutions (like reflection, plugins, or configuration files) would add unnecessary complexity for this project. 
//If the game were to scale up or require runtime extensibility, I would consider a more dynamic approach, but for now, this design balances maintainability and clarity.

