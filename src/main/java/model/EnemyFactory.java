package model;

import java.util.Random;

/**
 * A factory class for creating different types of enemies.
 * This adheres to the Open/Closed Principle by allowing new enemy types
 * to be added without modifying existing client code (like Main or CombatSystem),
 * as long as they extend the Enemy base class.
 */
public class EnemyFactory {
    private Random random = new Random();

    public Enemy createRandomEnemy() {
        int type = random.nextInt(3); // 0 for Goblin, 1 for Ghost, 2 for StoneMan
        if (type == 0) {
            return new Goblin();
        } else if (type == 1) {
            return new Ghost();
        } else if (type == 2) {
            return new StoneMan();
        } else {
            return new Goblin(); // Default case
        }
    }
}