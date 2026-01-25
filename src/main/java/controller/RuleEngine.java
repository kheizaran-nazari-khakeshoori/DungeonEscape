package controller;

import java.util.HashMap;
import java.util.Map;

// RuleEngine manages configurable game rules and probabilities (like enemy chance, trap chance, etc.).
// It allows getting and setting rule values dynamically for flexible gameplay adjustments.
public class RuleEngine {
   
    private final Map<String, Double> rules = new HashMap<>();

  
    private static final String ENEMY_CHANCE = "ENEMY_CHANCE";
    private static final String TRAP_CHANCE = "TRAP_CHANCE";
    private static final String FLEE_CHANCE = "FLEE_CHANCE";
    private static final String EMPTY_ROOM_CHANCE = "EMPTY_ROOM_CHANCE";
    private static final String ENEMY_HEALTH_SCALING = "ENEMY_HEALTH_SCALING";
    private static final String ENEMY_DAMAGE_SCALING = "ENEMY_DAMAGE_SCALING";
    private static final String POISON_RESISTANCE = "POISON_RESISTANCE";
    private static final String TRAP_DISARMCHANCE = "TRAP_DISARMCHANCE";
    
    

    public RuleEngine() {
       
        rules.put(ENEMY_CHANCE, 0.65); 
        rules.put(TRAP_CHANCE, 0.20); 
        rules.put(FLEE_CHANCE, 0.3); 
        rules.put(EMPTY_ROOM_CHANCE, 0.15);
        rules.put(ENEMY_HEALTH_SCALING, 0.3); 
        rules.put(ENEMY_DAMAGE_SCALING, 0.2); 
        rules.put(POISON_RESISTANCE, 1.0); 

    }

    public static String getEnemyChance() { return ENEMY_CHANCE; }
    public static String getTrapChance() { return TRAP_CHANCE; }
    public static String getFleeChance() { return FLEE_CHANCE; }
    public static String getEmptyRoomChance() { return EMPTY_ROOM_CHANCE; }
    public static String getEnemyHealthScaling() { return ENEMY_HEALTH_SCALING; }
    public static String getEnemyDamageScaling() { return ENEMY_DAMAGE_SCALING; }
    public static String getPoisonResistance() { return POISON_RESISTANCE; }
    public static String getTrapDisarmChance() { return TRAP_DISARMCHANCE; }

    public double getRule(String key) {
        return rules.getOrDefault(key, 0.0);
    }

    public void setRule(String key, double value) {
        rules.put(key, value);
    }
   
}
