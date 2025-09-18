package model;

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

    // Player attacks an enemy using the first weapon in inventory
    public void attack(Enemy enemy) {
        Weapon weapon = inventory.getFirstWeapon();
        if (weapon != null) {
            System.out.println(name + " attacks " + enemy.getName() + " with " + weapon.getName() + " for " + weapon.getDamage() + " damage!");
            enemy.takeDamage(weapon.getDamage());
            weapon.use(this); // reduce durability
        } else {
            System.out.println(name + " has no weapon to attack!");
        }
    }
}
