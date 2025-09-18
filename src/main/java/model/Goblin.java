package model;

public class Goblin extends Enemy {
    public Goblin() {
        super("Goblin", 50, 10);
    }

    @Override
    public void attack(Player player) {
        System.out.println(getName() + " attacks " + player.getName() + " for 10 damage!");
        player.takeDamage(10);
    }
}
