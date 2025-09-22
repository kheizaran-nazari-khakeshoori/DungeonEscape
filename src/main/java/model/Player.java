package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.InvalidMoveException;

public class Player {
    private String name;
    private static final int MAX_HEALTH = 100;
    private int health;
    private Inventory inventory;
    private List<Effect> activeEffects;
    private Weapon equippedWeapon;

    public Player(String name) {
        this.name = name;
        this.health = MAX_HEALTH;
        this.inventory = new Inventory();
        this.activeEffects = new ArrayList<>();
        this.equippedWeapon = null;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
        System.out.println(name + " took " + amount + " damage. Health now: " + health);
    }

    public void heal(int amount) {
        this.health += amount;
        // Enforce the max health rule here, strengthening encapsulation
        if (this.health > MAX_HEALTH) this.health = MAX_HEALTH;
        System.out.println(name + " healed for " + amount + ". Health now: " + this.health);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void showStatus() {
        System.out.println(name + " | Health: " + health + "/" + MAX_HEALTH);
        if (equippedWeapon != null) {
            System.out.println("Equipped: " + equippedWeapon.getName() + " (Durability: " +
                    equippedWeapon.getDurability() + ")");
        } else {
            System.out.println("Equipped: Fists");
        }
    }

    public void showInventory() {
        inventory.showInventory();
    }

    public void pickItem(Item item) {
        inventory.addItem(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    public void setEquippedWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    // Player attacks an enemy using the equipped weapon
    public void attack(Enemy enemy) {
        // This standard attack calls the overloaded version with 0 bonus damage.
        attack(enemy, 0);
    }

    // Overloaded attack method for special moves or bonuses (Overloading Polymorphism)
    public void attack(Enemy enemy, int bonusDamage) {
        if (equippedWeapon != null) {
            int totalDamage = equippedWeapon.getDamage() + bonusDamage;
            System.out.println(name + " attacks " + enemy.getName() + " with " + equippedWeapon.getName() +
                    " for " + totalDamage + " damage" + (bonusDamage > 0 ? " (" + bonusDamage + " bonus)!" : "!"));
            enemy.takeDamage(totalDamage);
            equippedWeapon.decreaseDurability();

            if (equippedWeapon.getDurability() <= 0) {
                System.out.println(equippedWeapon.getName() + " broke and was removed from inventory.");
                inventory.removeItem(equippedWeapon);
                equippedWeapon = null; // Unequip the broken weapon
            }
        } else {
            System.out.println(name + " has no weapon equipped to attack!");
        }
    }

    // Use item by name; throws exception if item not in inventory
    public void useItem(String itemName) throws InvalidMoveException {
        Item item = inventory.findItem(itemName);

        if (item != null) {
            item.use(this);  // Polymorphism: let the item decide what 'use' means

            // If the item reports that it is consumable, remove it after use.
            if (item.isConsumable()) {
                inventory.removeItem(item);
                System.out.println(item.getName() + " was consumed.");
            }
        } else {
            throw new InvalidMoveException("Item '" + itemName + "' is not in inventory!");
        }
    }

    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void applyTurnEffects() {
        if (activeEffects.isEmpty()) {
            return;
        }

        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.apply(this);
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
    }
}
