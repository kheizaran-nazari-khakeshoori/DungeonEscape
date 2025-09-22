package utils;

import java.util.Random;

public class DiceRoller {
    private Random random;

    public DiceRoller() {
        random = new Random();
    }

    // Expose the underlying Random object for methods that require it, like Collections.shuffle
    public Random getRandom() {
        return random;
    }

    // Roll a dice with given sides, e.g., roll(6) → 1..6
    public int roll(int sides) {
        if (sides <= 0) throw new IllegalArgumentException("Dice must have at least 1 side");
        return random.nextInt(sides) + 1;
    }

    // Roll a zero-based index for lists/arrays, e.g., rollIndex(3) -> 0, 1, or 2
    public int rollIndex(int bound) {
        if (bound <= 0) throw new IllegalArgumentException("Bound must be positive");
        return random.nextInt(bound);
    }
}
