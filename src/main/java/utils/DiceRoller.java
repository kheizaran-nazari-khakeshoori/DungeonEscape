package utils;

import java.util.Random;


public class DiceRoller {
    private final Random random;

    public DiceRoller() {
        random = new Random();
    }


    public Random getRandom() {
        return random;
    }


    public int roll(int sides) {
        if (sides <= 0) throw new IllegalArgumentException("Dice must have at least 1 side");
        return random.nextInt(sides) + 1;
    }

    
    public int rollIndex(int bound) {
        if (bound <= 0) throw new IllegalArgumentException("Bound must be positive");
        return random.nextInt(bound);
    }
}
