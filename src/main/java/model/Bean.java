package model;

public class Bean extends Player {
    public Bean() {
        // A rebellious princess, surprisingly tough.
        super("Bean", 100);
    }

    /**
     * Bean is tough and has a natural resistance to poison.
     */
    @Override
    public double getPoisonResistance() {
        return 0.5; // Bean takes only 50% damage from poison.
    }
}