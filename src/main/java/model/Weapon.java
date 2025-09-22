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
        // Using a weapon from the inventory means equipping it.
        player.setEquippedWeapon(this);
        System.out.println(player.getName() + " equipped " + getName() + ".");
    }

    public void decreaseDurability() {
        this.durability--;
    }
}
