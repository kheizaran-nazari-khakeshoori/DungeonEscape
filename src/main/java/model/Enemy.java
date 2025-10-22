package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.RuleEngine;
import utils.DiceRoller;

public abstract class Enemy implements ICombatant, ILootable {
    protected String name;//these are protected for subclass cunstructor access
    protected int maxHealth;
    protected int health;
    protected int baseDamage;
    protected int goldValue;
    protected String imagePath;
    protected DamageType damageType;//Association not composition 
    private final List<Effect<Enemy>> activeEffects;// composition , object contain other objects
    protected final Map<DamageType, Double> weaknesses;
    protected final Map<DamageType, Double> resistances;

    public Enemy(String name, int maxHealth, int baseDamage, int goldValue, String imagePath, DamageType damageType) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseDamage = baseDamage;
        this.goldValue = goldValue;
        this.imagePath = imagePath;
        this.damageType = damageType;
        this.weaknesses = new HashMap<>();
        this.resistances = new HashMap<>();
        this.activeEffects = new ArrayList<>();
    }

    /**
     * Protected method for subclasses to declare a weakness.
     * This improves encapsulation by preventing direct map modification.
     * @param type The DamageType the enemy is weak to.
     * @param multiplier The damage multiplier (e.g., 1.5 for +50% damage).
     */
    protected void addWeakness(DamageType type, double multiplier) {//opne/close principle 
        this.weaknesses.put(type, multiplier);//getting extra damage from specific damage type 
    }//subclasses add their own weakness 

    /**
     * Protected method for subclasses to declare a resistance.
     * @param type The DamageType the enemy is resistant to.
     * @param multiplier The damage multiplier (e.g., 0.75 for 25% resistance).
     */
    protected void addResistance(DamageType type, double multiplier) {
        this.resistances.put(type, multiplier);// getting less damage from specific damage type 
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) this.health = 0;
    }

    public String takeDamage(int amount, DamageType type) {
        double multiplier = 1.0; // calculate effectiveness
        String effectiveness = "";
        if (weaknesses.containsKey(type)) { // >> checking the damage type is in the map 
            multiplier = weaknesses.get(type); 
            effectiveness = "It's super effective!";
        } else if (resistances.containsKey(type)) {
            multiplier = resistances.get(type); 
            effectiveness = "It's not very effective...";
        }
        //apply damage 
        int finalDamage = (int) (amount * multiplier); // not polymorphism cause i manually casting 
        takeDamage(finalDamage);
        return effectiveness;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getHint() {
        if (!weaknesses.keySet().isEmpty()) {
            return "Hint: It seems weak to " + weaknesses.keySet().iterator().next().toString().toLowerCase() + " damage.";//using iterator pattern to get the first elemet from the set 
        }
        return "Hint: A standard foe.";
    }

    public void strengthen(int level, RuleEngine ruleEngine) {
        this.maxHealth += (int) (this.maxHealth * ruleEngine.getRule(RuleEngine.ENEMY_HEALTH_SCALING) * level);
        this.health = this.maxHealth;
        this.baseDamage += (int) (this.baseDamage * ruleEngine.getRule(RuleEngine.ENEMY_DAMAGE_SCALING) * level);
    }
    
    @Override
    public String applyTurnEffects() {
        // Placeholder for enemy effects
        return "";
    }

    @Override
    public Item dropLoot(DiceRoller dice) {
        // Default: no loot
        return null;
    }

    @Override
    public int getGoldValue() {
        return goldValue;
    }

    public int getBaseDamage() {
        return baseDamage;
    }


    
    @Override 
    public String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException{
        target.takeDamage(this.baseDamage);//run time polymorphism  and liskov principle 
        return this.name + " attacks " + target.getName() + " for " + this.baseDamage + " damage.";
    }
}