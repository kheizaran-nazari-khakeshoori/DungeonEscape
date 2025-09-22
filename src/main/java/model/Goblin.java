package model;

public class Goblin extends Enemy {
    public Goblin() {
        super("Goblin", 50, 10);
    }

    @Override
    public String attack(Player player) {
        player.takeDamage(getDamage());
        return getName() + " attacks " + player.getName() + " for " + getDamage() + " damage!";
    }
}
