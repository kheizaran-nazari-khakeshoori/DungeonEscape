package model;

public class Weapon extends Item {
    private int damage;
    private int durability;
    private final DamageType damageType;

    public Weapon(String name, int damage, int durability, DamageType damageType, String imagePath) {
        super(name, imagePath);
        this.damage = damage;
        this.durability = durability;
        this.damageType = damageType;
    }

    /**
     * The 'use' action for a weapon is to equip it.
     */
    @Override
    public void use(Player player) {
        player.setEquippedWeapon(this);
    }

    public int getDamage() {
        return damage;
    }

    public int getDurability() {
        return durability;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public void decreaseDurability() {
        if (this.durability > 0) {
            this.durability--;
        }
    }

    @Override
    public boolean isConsumable() {
        return false; // Weapons are not consumed when you 'use' (equip) them.
    }
}