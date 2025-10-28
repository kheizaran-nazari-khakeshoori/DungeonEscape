package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import controller.RuleEngine;
import exceptions.InvalidMoveException;
import utils.DiceRoller;

public abstract class Player implements Iwarrior {
    private final String name;
    private final int maxHealth;
    private int health;
    private final Inventory inventory;
    private final List<Effect<Player>> activeEffects;
    private Weapon equippedWeapon;
    private int gold; 
    
    
    protected int specialAbilityCooldownTurns;
    protected int currentSpecialAbilityCooldown;
    protected RuleEngine ruleEngine; 

    public Player(String name, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.inventory = new Inventory(); //creating inventory for the player 
        this.activeEffects = new ArrayList<>();//effect list for the player 
        this.equippedWeapon = null;
        this.gold = 0; // Starting gold
        this.specialAbilityCooldownTurns = 3; // Default cooldown of 3 turns
        this.currentSpecialAbilityCooldown = 0; // Starts ready to use
        this.ruleEngine = new RuleEngine(); // Each player gets a default rule engine
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

     
    public int getGold() {
        return gold;
    }
    public void addGold(int amount) {
        this.gold = gold + amount ;
        if (this.gold < 0) this.gold = 0;
    }
  

    
    public boolean spendGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold = gold - amount;
            return true;
        }
        return false;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void takeDamage(int amount) {
        int finalDamage = amount;
        
        for (Effect<Player> effect : activeEffects) {
            if (effect instanceof IDefensiveEffect iDefensiveEffect) {
                finalDamage = iDefensiveEffect.applyDefense(finalDamage);
            }
        }
        this.health = health - finalDamage;
        if (health < 0) health = 0;
    }


    @Override
    public String takeDamage(int amount, DamageType type) {
        takeDamage(amount); 
        return ""; 
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) this.health = this.maxHealth;
    }

    @Override
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

    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws InvalidMoveException {
        if (equippedWeapon != null) {
            int baseDamage = equippedWeapon.getDamage();
            
            
            String effectivenessMessage = target.takeDamage(baseDamage, equippedWeapon.getDamageType());
            
            equippedWeapon.decreaseDurability();

            String result = name + " attacks " + target.getName() + " with " + equippedWeapon.getName() +
                    " for " + baseDamage + " damage! " + effectivenessMessage;

            if (equippedWeapon.getDurability() <= 0) {
                inventory.removeItem(equippedWeapon);
                String brokenWeaponName = equippedWeapon.getName();
                setEquippedWeapon(null); // Unequip the broken weapon
                result += "\nYour " + brokenWeaponName + " broke!";
            }
            return result;
        } else {
            throw new InvalidMoveException("You have no weapon equipped to attack!");
        }
    }

   
   



    public void addEffect(Effect<Player> effect) { 
        activeEffects.add(effect);
    }

    public boolean hasEffect(String effectName) {
        return activeEffects.stream().anyMatch(effect -> effect.getName().equals(effectName));
    }

    public void removeEffect(String effectName) {
    Iterator<Effect<Player>> iterator = activeEffects.iterator();
    while (iterator.hasNext()) {
        Effect<Player> effect = iterator.next();
        if (effect.getName().equals(effectName)) {
            iterator.remove();
            break;
            }
        }
    }

    public void removeEffectsOfType(Class<?> effectType) {
    Iterator<Effect<Player>> iterator = activeEffects.iterator();
    while (iterator.hasNext()) {
        Effect<Player> effect = iterator.next();
        if (effectType.isInstance(effect)) {
            iterator.remove();
            }
        }
    }

  
    public List<Effect<Player>> getActiveEffects() {
        return Collections.unmodifiableList(activeEffects);
    }
    @Override
    public String applyTurnEffects() { // Renamed from getTurnEffectsResult for clarity
        if (activeEffects.isEmpty()) {
            return "";
        }
        
        StringBuilder effectsResult = new StringBuilder();
        Iterator<Effect<Player>> iterator = activeEffects.iterator(); // Use the generic iterator
        while (iterator.hasNext()) {
            Effect<Player> effect = iterator.next();
            String result = effect.apply(this); // polymorphism 
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

    
    public double getTrapDisarmChance() {
        return 0.33; // Base 33% chance for a normal player
    }

    
    public double getPoisonResistance() {
        return ruleEngine.getRule(RuleEngine.POISON_RESISTANCE);
    }

    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

   
    public abstract String useSpecialAbility(Enemy enemy, DiceRoller dice);

    
    public boolean isSpecialAbilityReady() {
        return currentSpecialAbilityCooldown == 0;
    }

    
    public void tickCooldowns() {
        if (currentSpecialAbilityCooldown > 0) {
            currentSpecialAbilityCooldown--;
        }
    }

    public int getCurrentSpecialAbilityCooldown() {//how many turns are left to cooldown
        return currentSpecialAbilityCooldown;
    }

    
    protected void putSpecialAbilityOnCooldown() { this.currentSpecialAbilityCooldown = this.specialAbilityCooldownTurns; } 
}
