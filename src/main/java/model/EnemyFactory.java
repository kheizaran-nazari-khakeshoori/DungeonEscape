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
        switch (type) {
            case 0: return new Goblin();
            case 1: return new Ghost();
            case 2: return new StoneMan();
            default: return new Goblin(); // Default case
        }
    }
}