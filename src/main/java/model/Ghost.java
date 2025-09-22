package model;

public class Ghost extends Enemy {
    public Ghost() {
        super("Ghost", 40, 15);
    }

    @Override
    public String attack(Player player) {
        player.takeDamage(getDamage());
        return getName() + " haunts " + player.getName() + " for " + getDamage() + " damage!";
    }

    @Override
    public void move() {
        System.out.println(getName() + " floats silently!");
    }
}
