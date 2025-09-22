package model;

import java.util.Random;

/**
 * A factory class for creating different types of enemies.
 * This adheres to the Open/Closed Principle by allowing new enemy types
 * to be added without modifying existing client code (like Main or CombatSystem),
 * as long as they extend the Enemy base class.
 */
public class EnemyFactory {
    private final Random random;

    public EnemyFactory(Random random) {
        this.random = random;
    }

    public Enemy createRandomEnemy() {
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                return new Goblin();
            case 1:
                return new Ghost();
            case 2:
                return new StoneMan();
            default: // This case should not be reached with nextInt(3)
                return new Goblin();
        }
    }
}