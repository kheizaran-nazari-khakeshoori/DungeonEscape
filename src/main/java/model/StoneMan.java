package model;

public class StoneMan extends Enemy {
    public StoneMan() {
        super("StoneMan", 100, 20);
    }

    @Override
    public void attack(Player player) {
        System.out.println(getName() + " crushes " + player.getName() + " with a rock for " + getDamage() + " damage!");
        player.takeDamage(getDamage());
    }

    @Override
    public void move() {
        System.out.println(getName() + " lumbers slowly forward.");
    }
}
