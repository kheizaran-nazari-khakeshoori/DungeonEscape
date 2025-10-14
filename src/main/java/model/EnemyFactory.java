package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;

/**
 * A factory class for creating different types of enemies.
 * This design makes it easy to add new enemy types.
 */
public class EnemyFactory {
    private final DiceRoller dice;
    private final List<Supplier<Enemy>> enemySuppliers = new ArrayList<>();

    public EnemyFactory(DiceRoller dice) {
        this.dice = dice;
        // Register all available enemy types here.
        // To add a new enemy, just create its class and add it to this list.
        enemySuppliers.add(Goblin::new);
        enemySuppliers.add(Ghost::new);
        enemySuppliers.add(StoneMan::new);
        enemySuppliers.add(SkeletonWarrior::new);
        // Adding the rest of my custom enemies
        enemySuppliers.add(MimicChest::new);
        enemySuppliers.add(PoisonSpider::new);
        enemySuppliers.add(ShadowAssassin::new);
        enemySuppliers.add(SlimeBlob::new);
    }

    public Enemy createRandomEnemy() {
        if (enemySuppliers.isEmpty()) {
            // Fallback in case no enemies are registered.
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