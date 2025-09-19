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
    private List<ActiveEffect> activeEffects;

    public Player(String name) {
        this.name = name;
        this.health = MAX_HEALTH;
        this.inventory = new Inventory();
        this.activeEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
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

    public void showInventory() {
        inventory.showInventory();
    }

    public void pickItem(Item item) {
        inventory.addItem(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    // Player attacks an enemy using the first weapon
    public void attack(Enemy enemy) {
        Weapon weapon = inventory.getFirstWeapon();
        if (weapon != null) {
            System.out.println(name + " attacks " + enemy.getName() + " with " + weapon.getName() +
                    " for " + weapon.getDamage() + " damage!");
            enemy.takeDamage(weapon.getDamage());
            weapon.use(this); // Decrease durability

            if (weapon.getDurability() <= 0) {
                inventory.removeItem(weapon);
                System.out.println(weapon.getName() + " broke and was removed from inventory.");
            }
        } else {
            System.out.println(name + " has no weapon to attack!");
        }
    }

    // Use item by name; throws exception if item not in inventory
    public void useItem(String itemName) throws InvalidMoveException {
        Item item = inventory.findItem(itemName);

        if (item != null) {
            item.use(this);  // Polymorphism: let the item decide what 'use' means

            // Consumable items like potions should be removed after use.
            if (item instanceof Potion) {
                inventory.removeItem(item);
                System.out.println(item.getName() + " was consumed.");
            }
        } else {
            throw new InvalidMoveException("Item '" + itemName + "' is not in inventory!");
        }
    }

    public void addEffect(ActiveEffect effect) {
        activeEffects.add(effect);
    }

    public void applyTurnEffects() {
        if (activeEffects.isEmpty()) {
            return;
        }

        Iterator<ActiveEffect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            ActiveEffect effect = iterator.next();
            effect.tick(this);
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
    }
}
