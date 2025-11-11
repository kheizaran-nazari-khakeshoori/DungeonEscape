package model;

import java.util.HashMap;
import java.util.Map;

import controller.EffectManager;
import controller.RuleEngine;
import utils.DiceRoller;

public abstract class Enemy implements Iwarrior, ITakeable,IOperation_on_Effect<Enemy> {
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int baseDamage;
    protected int goldValue;
    protected String imagePath;
    protected DamageType damageType;
    private final EffectManager<Enemy> effectManager;
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
        this.effectManager = new EffectManager<>();
    }

    
    protected void addWeakness(DamageType type, double multiplier) {
        this.weaknesses.put(type, multiplier);
    }

    
    protected void addResistance(DamageType type, double multiplier) {
        this.resistances.put(type, multiplier);
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
        takeDamage(amount, null);
    }

    @Override
    public String takeDamage(int amount, DamageType type) {
        double multiplier = 1.0; 
        String effectiveness = "";

        if (weaknesses.containsKey(type)) {
            multiplier = weaknesses.get(type); 
            effectiveness = "It's super effective!";

        } 
        else if (resistances.containsKey(type)) {
            multiplier = resistances.get(type); 
            effectiveness = "It's not very effective...";
        }
        int damagecaused = (int) (amount * multiplier); 
        int finalDamage = damagecaused;    
        this.health -= finalDamage;
        if (this.health < 0) this.health = 0;
    
        return effectiveness;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getHint() {
        if(weaknesses.size()> 0)
        {
            DamageType firstWeakness = weaknesses.keySet().iterator().next();
            String weaknessName = firstWeakness.toString().toLowerCase();
            return "Hint: It seems weak to " + weaknessName + " damage.";
        }
        return "Hint:normal enemy";
    }

    public void strengthen(int level , RuleEngine ruleEngine)
    {
        double health_scaling = ruleEngine.getRule (RuleEngine.ENEMY_HEALTH_SCALING);
        double damage_scaling = ruleEngine.getRule (RuleEngine.ENEMY_DAMAGE_SCALING);

        int healthIncrease = (int) (this.maxHealth * health_scaling * level);
        this.maxHealth = maxHealth + healthIncrease;

        int damageIncrease = (int) (this.baseDamage * damage_scaling * level );
        this.baseDamage = baseDamage + damageIncrease ;
    }
    
    @Override
    public String applyTurnEffects() {
         return effectManager.applyAllEffects(this);
       
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
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException{
        target.takeDamage(this.baseDamage);
        return getAttackMessage();
    }

    protected abstract String getAttackMessage();
    
    @Override
    public void addEffect(Effect<Enemy> effect) {
       effectManager.addEffect(effect);
    }


    @Override
    public boolean hasEffect(String effectName) {
        return effectManager.hasEffect(effectName);
    }

    @Override
    public void removeEffect(String effectName) {
         effectManager.removeEffect(effectName);
        }


    @Override
    public void removeEffectsOfType(Class<?> effectType) {
        effectManager.removeEffectsOfType(effectType);
    }

}



