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

    // Encapsulation: private fields with getters/setters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
        System.out.println(name + " took " + amount + " damage. Health now: " + health);
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Abstraction: all enemies must implement attack
    public abstract void attack(Player player);

    // Polymorphism (inclusion): subclasses can override move()
    public void move() {
        System.out.println(name + " moves cautiously.");
    }
}
