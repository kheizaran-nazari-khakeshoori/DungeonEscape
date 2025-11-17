package controller;

import java.util.HashMap;
import java.util.Map;


public class RuleEngine implements Cloneable {
   
    private Map<String, Double> rules = new HashMap<>();

  
    public static final String ENEMY_CHANCE = "ENEMY_CHANCE";
    public static final String TRAP_CHANCE = "TRAP_CHANCE";
    public static final String FLEE_CHANCE = "FLEE_CHANCE";
    public static final String ENEMY_HEALTH_SCALING = "ENEMY_HEALTH_SCALING";
    public static final String ENEMY_DAMAGE_SCALING = "ENEMY_DAMAGE_SCALING";
    public static final String POISON_RESISTANCE = "POISON_RESISTANCE";
    public static final String SUPER_POWER = "SUPER_POWER";
    

    public RuleEngine() {
       
        rules.put(ENEMY_CHANCE, 0.75); 
        rules.put(TRAP_CHANCE, 0.1); 
        rules.put(FLEE_CHANCE, 0.5); 
        rules.put(ENEMY_HEALTH_SCALING, 0.2); 
        rules.put(ENEMY_DAMAGE_SCALING, 0.1); 
        rules.put(POISON_RESISTANCE, 1.0); 
        rules.put(SUPER_POWER , 0.85);

    }

    public double getRule(String key) {
        return rules.getOrDefault(key, 0.0);
    }

    public void setRule(String key, double value) {
        rules.put(key, value);
    }

    @Override  
    public RuleEngine clone() throws CloneNotSupportedException {
        try {
            RuleEngine cloned = (RuleEngine) super.clone();
            cloned.rules = new HashMap<>(this.rules);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  
        }
    }
}
