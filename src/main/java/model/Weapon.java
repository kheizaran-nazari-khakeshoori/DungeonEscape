package model;

public class Weapon extends Item {
    private int damage;
    private int durability;

    public Weapon(String name, int damage, int durability) {
        super(name);
        this.damage = damage;
        this.durability = durability;
    }

    public int getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public void use(Player player) {
        System.out.println(getName() + " is used to attack, dealing " + damage + " damage!");
        // Optionally decrease durability
        durability--;
        if (durability <= 0) {
            System.out.println(getName() + " broke!");
        }
    }
}

