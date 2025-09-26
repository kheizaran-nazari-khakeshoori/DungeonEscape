package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controller.RuleEngine;
import exceptions.InvalidMoveException;
import utils.DiceRoller;

public abstract class Player {
    private String name;
    private int maxHealth;
    private int health;
    private Inventory inventory;
    private final List<Effect<Player>> activeEffects;
    private Weapon equippedWeapon;
    private int gold; // The amount of gold the player has
    // Cooldown fields
    protected int specialAbilityCooldownTurns;
    protected int currentSpecialAbilityCooldown;
    protected RuleEngine ruleEngine; // Each player can have their own set of rules

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
        this.gold = 0; // Starting gold
        this.specialAbilityCooldownTurns = 3; // Default cooldown of 3 turns
        this.currentSpecialAbilityCooldown = 0; // Starts ready to use
        this.ruleEngine = new RuleEngine(); // Each player gets a default rule engine
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

     // Gold related methods
    public int getGold() {
        return gold;
    }
    public void addGold(int amount) {
        this.gold += amount;
        if (this.gold < 0) this.gold = 0; // Prevent negative gold
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
    public String attack(Enemy enemy, DiceRoller dice) {
        // This standard attack calls the overloaded version with 0 bonus damage.
        return attack(enemy, 0, dice);
    }

    // Overloaded attack method for special moves or bonuses (Overloading Polymorphism)
    public String attack(Enemy enemy, int bonusDamage, DiceRoller dice) {
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

    public void addEffect(Effect<Player> effect) { // Now correctly typed
        activeEffects.add(effect);
    }

    public boolean hasEffect(String effectName) {
        return activeEffects.stream().anyMatch(effect -> effect.getName().equals(effectName));
    }

    public void removeEffect(String effectName) {
        activeEffects.removeIf(effect -> effect.getName().equals(effectName));
    }

    public String applyTurnEffects() { // Renamed from getTurnEffectsResult for clarity
        if (activeEffects.isEmpty()) {
            return "";
        }
        
        StringBuilder effectsResult = new StringBuilder();
        Iterator<Effect<Player>> iterator = activeEffects.iterator(); // Use the generic iterator
        while (iterator.hasNext()) {
            Effect<Player> effect = iterator.next();
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

    /**
     * Gets the player's chance to successfully disarm or avoid a trap.
     * @return A value between 0.0 and 1.0 representing the percentage chance.
     */
    public double getTrapDisarmChance() {
        return 0.33; // Base 33% chance for a normal player
    }

    /**
     * Gets the player's natural resistance to poison.
     * @return A multiplier for incoming poison damage (e.g., 1.0 for full damage, 0.5 for half).
     */
    public double getPoisonResistance() {
        return 1.0; // Default: takes full damage from poison.
    }

    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

    /**
     * Abstract method for a character's unique special ability.
     * Each subclass must provide its own implementation.
     * @param enemy The target of the ability, if applicable.
     * @param dice The dice roller for any random effects.
     */
    public abstract String useSpecialAbility(Enemy enemy, DiceRoller dice);

    // --- Cooldown Methods ---

    /**
     * Checks if the special ability can be used.
     * @return true if the cooldown is 0, false otherwise.
     */
    public boolean isSpecialAbilityReady() {
        return currentSpecialAbilityCooldown == 0;
    }

    /**
     * Reduces the current cooldown by one turn. Called at the start of a player's turn.
     */
    public void tickCooldowns() {
        if (currentSpecialAbilityCooldown > 0) {
            currentSpecialAbilityCooldown--;
        }
    }

    public int getCurrentSpecialAbilityCooldown() {
        return currentSpecialAbilityCooldown;
    }

    // This should be called by subclasses within their useSpecialAbility implementation.
    protected void putSpecialAbilityOnCooldown() { this.currentSpecialAbilityCooldown = this.specialAbilityCooldownTurns; }
}
