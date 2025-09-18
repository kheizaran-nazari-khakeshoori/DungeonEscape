package model;

public class Goblin extends Enemy {
    public Goblin() {
        super("Goblin", 50, 10);
    }

    @Override
    public void attack(Player player) {
        System.out.println(getName() + " swings a club at " + player.getName() + " for " + getDamage() + " damage!");
        // You could implement player.takeDamage(getDamage()) if Player supports it
    }

    @Override
    public void move() {
        System.out.println(getName() + " scurries quickly!");
    }
}
