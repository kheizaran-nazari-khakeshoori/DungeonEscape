package model;

public class Bean extends Player {
    public Bean() {
        // A rebellious princess, surprisingly tough.
        super("Bean", 100);

        Weapon sword = new Weapon("Bean's Sword", 15, 35, DamageType.SLASHING, "images/weapons/Sword.png");
        this.pickItem(sword);
    }
}