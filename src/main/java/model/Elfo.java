package model;

public class Elfo extends Player {
    public Elfo() {
        // An optimistic elf, not built for combat but surprisingly resilient.
        super("Elfo", 75);

        Weapon sword = new Weapon("Sword", 12, 40, DamageType.SLASHING, "images/weapons/Sword.png");
        this.pickItem(sword);
    }
}