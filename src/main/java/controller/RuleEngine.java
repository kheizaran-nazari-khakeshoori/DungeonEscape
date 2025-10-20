package controller;

import java.util.HashMap;
import java.util.Map;


public class RuleEngine implements Cloneable {//for making copy of it safely (the class i mean)

    // Using a Map allows for adding new rules without changing the class structure.
    //like a page of rule-book 
    //each rule has a title (string) and a value (double)
    private Map<String, Double> rules = new HashMap<>();

    // Rule Keys - Using constants prevents typos.
    public static final String ENEMY_CHANCE = "ENEMY_CHANCE";
    public static final String TRAP_CHANCE = "TRAP_CHANCE";
    public static final String FLEE_CHANCE = "FLEE_CHANCE";
    public static final String ENEMY_HEALTH_SCALING = "ENEMY_HEALTH_SCALING";
    public static final String ENEMY_DAMAGE_SCALING = "ENEMY_DAMAGE_SCALING";
    public static final String POISON_RESISTANCE = "POISON_RESISTANCE";


    public RuleEngine() {
        // In the future, this constructor could load rules from a file.
        // --- Default Game Rules ---
        rules.put(ENEMY_CHANCE, 0.75); // 75% chance for an enemy
        rules.put(TRAP_CHANCE, 0.15); // 15% chance for a trap. (10% chance for an empty room)
        rules.put(FLEE_CHANCE, 0.5); // 50% chance to flee successfully
        rules.put(ENEMY_HEALTH_SCALING, 0.2); // +20% health per level
        rules.put(ENEMY_DAMAGE_SCALING, 0.1); // +10% damage per level
        rules.put(POISON_RESISTANCE, 1.0); // Default: 100% damage taken
    }

    public double getRule(String key) {//look up a rule in the book
        return rules.getOrDefault(key, 0.0);
    }

    public void setRule(String key, double value) {//change ruke 
        rules.put(key, value);
    }

    @Override
    public RuleEngine clone() {// i do not want to give all the characters the same rule-book 
        try {
            RuleEngine cloned = (RuleEngine) super.clone();//shallow copy (making the cover)
            cloned.rules = new HashMap<>(this.rules);//make the copy deep 
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen cause the class implement cloneable , i gave the permission already 
        }
    }
}
