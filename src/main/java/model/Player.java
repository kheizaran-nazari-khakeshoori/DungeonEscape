package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exceptions.InvalidMoveException;

public class Player {
    private String name;
    private int maxHealth;
    private int health;
    private Inventory inventory;
    private List<Effect> activeEffects;
    private Weapon equippedWeapon;

    public Player(String name) {
        this(name, 100); // Default to 100 health
    }

    public Player(String name, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
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

    public int getMaxHealth() {
        return maxHealth;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        this.health += amount;
        // Enforce the max health rule here, strengthening encapsulation
        if (this.health > this.maxHealth) this.health = this.maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void pickItem(Item item) {
        inventory.addItem(item);
    }

    public void setEquippedWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    // Player attacks an enemy using the equipped weapon
    public String attack(Enemy enemy) {
        // This standard attack calls the overloaded version with 0 bonus damage.
        return attack(enemy, 0);
    }

    // Overloaded attack method for special moves or bonuses (Overloading Polymorphism)
    public String attack(Enemy enemy, int bonusDamage) {
        if (equippedWeapon != null) {
            int baseDamage = equippedWeapon.getDamage() + bonusDamage;
            
            // The takeDamage method now returns a string about effectiveness
            String effectivenessMessage = enemy.takeDamage(baseDamage, equippedWeapon.getDamageType());
            
            equippedWeapon.decreaseDurability();

            String result = name + " attacks " + enemy.getName() + " with " + equippedWeapon.getName() +
                    " for " + baseDamage + " damage! " + effectivenessMessage;
            if (bonusDamage > 0) result += " (" + bonusDamage + " bonus)!";

            if (equippedWeapon.getDurability() <= 0) {
                inventory.removeItem(equippedWeapon);
                String brokenWeaponName = equippedWeapon.getName();
                setEquippedWeapon(null); // Unequip the broken weapon
                result += "\nYour " + brokenWeaponName + " broke!";
            }
            return result;
        }
        return name + " has no weapon equipped to attack!";
    }

    // Use item by name; throws exception if item not in inventory
    public void useItem(String itemName) throws InvalidMoveException {
        Item item = getInventory().findItem(itemName);
        if (item == null) {
            throw new InvalidMoveException("Item '" + itemName + "' is not in inventory!");
        }

        // Let the item affect the player (Polymorphism)
        item.use(this);

        // If the item is consumable, the player removes it from their own inventory.
        if (item.isConsumable()) {
            getInventory().removeItem(item);
        }
    }

    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public boolean hasEffect(String effectName) {
        return activeEffects.stream().anyMatch(effect -> effect.getName().equals(effectName));
    }

    public void removeEffect(String effectName) {
        activeEffects.removeIf(effect -> effect.getName().equals(effectName));
    }

    public String getTurnEffectsResult() {
        if (activeEffects.isEmpty()) {
            return "";
        }
        
        StringBuilder effectsResult = new StringBuilder();
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            String result = effect.apply(this); // Get result message from effect
            if (result != null && !result.isEmpty()) {
                if (effectsResult.length() > 0) effectsResult.append("\n");
                effectsResult.append(result);
            }
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        return effectsResult.toString();
    }

    public void applyTurnEffects() {
        getTurnEffectsResult(); // We just call the new method but don't need its return value here
    }

    /**
     * Gets the player's chance to successfully disarm or avoid a trap.
     * @return A value between 0.0 and 1.0 representing the percentage chance.
     */
    public double getTrapDisarmChance() {
        return 0.33; // Base 33% chance for a normal player
    }
}
