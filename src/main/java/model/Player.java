package model;

import exceptions.InvalidMoveException;

public class Player {
    private String name;
    private int health;
    private Inventory inventory;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.inventory = new Inventory();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
    // Add inside Player class
    public void setHealth(int health) {
        this.health = health;
    }


    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
        System.out.println(name + " took " + amount + " damage. Health now: " + health);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Inventory getInventory() {
        return inventory;
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
            weapon.use(this); // decrease durability or trigger effects
        } else {
            System.out.println(name + " has no weapon to attack!");
        }
    }

    // Use item by name; throws exception if item not in inventory
    public void useItem(String itemName) throws InvalidMoveException {
        boolean found = false;
        for (Item item : inventory.getItems()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.use(this);  // polymorphism: Weapon or Potion
                found = true;
                break;
            }
        }
        if (!found) {
            throw new InvalidMoveException("Item '" + itemName + "' is not in inventory!");
        }
    }
}
