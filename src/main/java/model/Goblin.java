package model;

public class Goblin extends Enemy {

    public Goblin() {
        // Name, Health, Base Damage, Image Path
        super("Goblin", 30, 8, "images/enemies/Goblin.png");
        // Goblins have no special resistances or weaknesses.
    }

    @Override
    public String attack(Player player) {
        player.takeDamage(this.baseDamage);
        return this.name + " attacks you for " + this.baseDamage + " damage.";
    }

    @Override
    public String getHint() {
        return "Hint: This creature looks straightforward. A standard weapon should suffice.";
    }
}