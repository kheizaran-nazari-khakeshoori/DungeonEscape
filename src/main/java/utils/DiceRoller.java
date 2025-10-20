package utils;

import java.util.Random;

//this class handles the randomness needed for my game 
public class DiceRoller {
    private Random random;

    public DiceRoller() {
        random = new Random();
    }


    public Random getRandom() {
        return random;
    }

    // Roll a dice with given sides , helper method 
    public int roll(int sides) {//simulating rolling a dice 
        if (sides <= 0) throw new IllegalArgumentException("Dice must have at least 1 side");
        return random.nextInt(sides) + 1;
    }


    public int rollIndex(int bound) {//getting random index for arrays or lists 
        if (bound <= 0) throw new IllegalArgumentException("Bound must be positive");
        return random.nextInt(bound);
    }
}
