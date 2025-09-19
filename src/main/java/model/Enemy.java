package model;

public abstract class Enemy {
    private String name;
    private int health;
    private int damage;

    public Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public boolean isAlive() { return health > 0; }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
        System.out.println(name + " took " + amount + " damage. Health now: " + health);
    }

    public abstract void attack(Player player);

    public void move() {
        System.out.println(name + " moves cautiously.");
    }
}
