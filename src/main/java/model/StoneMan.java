package model;

public class StoneMan extends Enemy {
    public StoneMan() {
        super("StoneMan", 100, 20);
    }

    @Override
    public String attack(Player player) {
        player.takeDamage(getDamage());
        return getName() + " crushes " + player.getName() + " with a rock for " + getDamage() + " damage!";
    }

    @Override
    public void move() {
        System.out.println(getName() + " lumbers slowly forward.");
    }
}
