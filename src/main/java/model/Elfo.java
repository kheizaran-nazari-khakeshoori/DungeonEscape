package model;

public class Elfo extends Player {
    public Elfo() {
        // An optimistic elf, not built for combat but surprisingly resilient.
        super("Elfo", 75);
    }

    /**
     * Elfo is nimble and has a better chance of avoiding traps.
     */
    @Override
    public double getTrapDisarmChance() {
        return 0.66; // Elfo has a 66% chance to succeed.
    }
}