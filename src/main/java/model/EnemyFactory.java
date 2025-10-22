package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;
//here i am using factory pattern 


public class EnemyFactory {
    private final DiceRoller dice;
    private final List<Supplier<Enemy>> enemySuppliers = new ArrayList<>();//depends on abstract class >> DIP 
//supplier <T> >> functional interface from java 8
    public EnemyFactory(DiceRoller dice) {//cunstructor dependacy injection 
        this.dice = dice;
        // fill that one array list eith 8 suppliers 
        enemySuppliers.add(Goblin::new);
        enemySuppliers.add(Ghost::new);
        enemySuppliers.add(StoneMan::new);
        enemySuppliers.add(SkeletonWarrior::new);
        enemySuppliers.add(MimicChest::new);
        enemySuppliers.add(PoisonSpider::new);
        enemySuppliers.add(ShadowAssassin::new);
        enemySuppliers.add(SlimeBlob::new);
    }

    public Enemy createRandomEnemy() {//creating and returing random enemy 
        if (enemySuppliers.isEmpty()) {
            
            System.err.println("Warning: No enemies registered in EnemyFactory. Defaulting to Goblin.");
            return new Goblin();//preventing crashes 
        }
        // Pick a random enemy supplier from the list and create a new instance.
        int index = dice.rollIndex(enemySuppliers.size());//generate random index >> strategy pettern 
        return enemySuppliers.get(index).get();
    }//                         get     call 

    /**
     * Returns a copy of the list of enemy suppliers.
     * @return A new list containing all registered enemy suppliers.
     */
    public List<Supplier<Enemy>> getEnemySuppliers() {// returning a copy >> info hiding 
        return new ArrayList<>(enemySuppliers);
    }
}