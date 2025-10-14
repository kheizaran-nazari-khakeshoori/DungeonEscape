package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.DiceRoller;

public abstract class Enemy implements ICombatant, ILootable {
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int baseDamage;
    protected int goldValue;
    protected String imagePath;
    protected DamageType damageType;
    private final List<Effect<Enemy>> activeEffects;
    protected Map<DamageType, Double> weaknesses;
    protected Map<DamageType, Double> resistances;

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
        double multiplier = 1.0;
        String effectiveness = "";
        if (weaknesses.containsKey(type)) {
            multiplier = 1.5;
            effectiveness = "It's super effective!";
        } else if (resistances.containsKey(type)) {
            multiplier = 0.5;
            effectiveness = "It's not very effective...";
        }
        int finalDamage = (int) (amount * multiplier);
        takeDamage(finalDamage);
        return effectiveness;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getHint() {
        if (!weaknesses.keySet().isEmpty()) {
            return "Hint: It seems weak to " + weaknesses.keySet().iterator().next().toString().toLowerCase() + " damage.";
        }
        return "Hint: A standard foe.";
    }

    public void strengthen(int level, controller.RuleEngine ruleEngine) {
        this.maxHealth += (int) (this.maxHealth * ruleEngine.getRule(controller.RuleEngine.ENEMY_HEALTH_SCALING) * level);
        this.health = this.maxHealth;
        this.baseDamage += (int) (this.baseDamage * ruleEngine.getRule(controller.RuleEngine.ENEMY_DAMAGE_SCALING) * level);
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
        if (target instanceof Player) {
            Player player = (Player) target;
            player.takeDamage(this.baseDamage);
            return this.name + " attacks " + player.getName() + " for " + this.baseDamage + " damage.";
        }
        // Generic attack for other ICombatant types
        target.takeDamage(this.baseDamage);
        return this.name + " attacks " + target.getName() + " for " + this.baseDamage + " damage.";
    }
}