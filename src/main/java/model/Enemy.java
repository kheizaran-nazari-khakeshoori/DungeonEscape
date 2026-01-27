package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.EffectManager;
import controller.RuleEngine;
import utils.DiceRoller;

public abstract class Enemy implements Iwarrior, ITakeable, IEffectable<Enemy>, Encounter {
    private String name;
    private int maxHealth;
    private  int health;
    private int baseDamage;
    private final int goldValue;
    private final String imagePath;
    private final EffectManager<Enemy> effectManager;
    private  final Map<DamageType, Double> weaknesses;
    private final Map<DamageType, Double> resistances;
    

    public Enemy(String name, int maxHealth, int baseDamage, int goldValue, String imagePath, DamageType damageType) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseDamage = baseDamage;
        this.goldValue = goldValue;
        this.imagePath = imagePath;
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

    protected void setHealth(int health)
    {

        if(health < 0)
        {
            this.health = 0;
        }
        else if (health > maxHealth)
        {
            this.health = maxHealth;
        }
        else
        {
            this.health = health;
        }
        
    }

    protected void setBaseDamage(int baseDamage)
    {
        if(baseDamage < 0)
        {
            this.baseDamage = 0;
        }
        else if (baseDamage > 100)
        {
            this.baseDamage = 100;
        }

        else
        {
            this.baseDamage = baseDamage;
        }
    }

    protected void setName(String name)
    {
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Name cannot be empty!");
        }
        this.name = name;
    }
    

    protected void setMaxHealth(int maxHealth) 
    {
        if (maxHealth <= 0) 
        {
            throw new IllegalArgumentException("Max health must be positive");
        }
        this.maxHealth = maxHealth;
    
        // Keep health within bounds:
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
         }
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
        if(!weaknesses.isEmpty())
        {
            DamageType firstWeakness = weaknesses.keySet().iterator().next();
            String weaknessName = firstWeakness.toString().toLowerCase();
            return "Hint: It seems weak to " + weaknessName + " damage.";
        }
        return "Hint:normal enemy";
    }

    public void strengthen(int level , RuleEngine ruleEngine)
    {
        if(level < 1)
        {
            throw new IllegalArgumentException(" level must start from 1");
        }

        double health_scaling = ruleEngine.getRule(RuleEngine.getEnemyHealthScaling());
        double damage_scaling = ruleEngine.getRule(RuleEngine.getEnemyDamageScaling());

        for(int i = 0 ; i < level ; i++)
        {
            int newMaxHealth = (int) (getMaxHealth() * (1 + health_scaling));
            this.maxHealth = newMaxHealth;
            setHealth(getMaxHealth());

            int newDamage = (int)(getBaseDamage() * (1 + damage_scaling));
            setBaseDamage(newDamage);

        }

        if (level > 1) {
        this.name = this.name + " Lv" + level;
        }
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
    public String attack(Iwarrior target, DiceRoller dice) {
        target.takeDamage(this.baseDamage);
        return getAttackMessage();
    }

    protected abstract String getAttackMessage();
    
    @Override
    
    public void addEffect(Effect<Enemy> effect) {
       effectManager.addEffect(effect);
    }
    //

    @Override
    public boolean hasEffect(String effectName) {
        return effectManager.hasEffect(effectName);
    }

    @Override
    public void removeEffect(String effectName) {
         effectManager.removeEffect(effectName);
        }


    @Override
    public void removeEffectsOfType(Class<? extends Effect<Enemy>> effectType) {
        effectManager.removeEffectsOfType(effectType);
    }

    @Override
    public List<Effect<Enemy>> getActiveEffects() {
        return effectManager.getActiveEffects();
    }
}

/**
 * I kept the damage calculation logic inside the Enemy class because:
 * 
 * 1. The calculation directly depends on the Enemy's own fields (health, weaknesses, resistances).
 * 2. The logic is closely tied to the Enemy's state and behavior, making it a natural responsibility for the class.
 * 3. Moving this logic to a separate class would require exposing internal state (like health, weaknesses, resistances) through getters and setters, which can break encapsulation and make the code harder to maintain.
 * 4. The calculation is not duplicated elsewhere (e.g., Player may have different logic), so there is no code repetition to eliminate.
 * 5. Keeping it here makes the code easier to read and follow, as all combat-related logic for Enemy is in one place.
 * 
 * If the project grows and I see that damage calculation is shared and identical across multiple classes, or becomes more complex, I will consider refactoring it into a utility or strategy class.
 */
// Enums provide type safety, compile-time checking, and prevent typos. String would allow takeDamage(10, "FIER") to compile but fail silently.
