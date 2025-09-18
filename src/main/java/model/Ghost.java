package model;

public class Ghost extends Enemy {
    public Ghost() {
        super("Ghost", 40, 15);
    }

    @Override
    public void attack(Player player) {
        System.out.println(getName() + " haunts " + player.getName() + " for " + getDamage() + " damage!");
    }

    @Override
    public void move() {
        System.out.println(getName() + " floats silently!");
    }
}
