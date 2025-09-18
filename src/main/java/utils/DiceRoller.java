package util;

import java.util.Random;

public class DiceRoller {
    private Random random;

    public DiceRoller() {
        random = new Random();
    }

    // Roll a dice with given sides, e.g., roll(6) → 1..6
    public int roll(int sides) {
        if (sides <= 0) throw new IllegalArgumentException("Dice must have at least 1 side");
        return random.nextInt(sides) + 1;
    }
}

