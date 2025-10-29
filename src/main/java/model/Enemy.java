package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.RuleEngine;
import utils.DiceRoller;

public abstract class Enemy implements Iwarrior, ITakeable {
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int baseDamage;
    protected int goldValue;
    protected String imagePath;
    protected DamageType damageType;
    private final List<Effect<Enemy>> activeEffects;
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
        int finalDamage = amount;
        for (Effect<Enemy> effect : activeEffects)
        {
            if(effect instanceof  IDefensiveEffect  iDefensiveEffect)
            {
                finalDamage = iDefensiveEffect.applyDefense(finalDamage);
            }
        }
        this.health = health - finalDamage;
        if (this.health < 0) this.health = 0;
    }

    @Override
    public String takeDamage(int amount, DamageType type) {
        double multiplier = 1.0; 
        String effectiveness = "";

        if (weaknesses.containsKey(type)) {
            multiplier = weaknesses.get(type); 
            effectiveness = "It's super effective!";

        } else if (resistances.containsKey(type)) {
            multiplier = resistances.get(type); 
            effectiveness = "It's not very effective...";
        }
        int damagecaused = (int) (amount * multiplier); 
        int finalDamage = damagecaused;
       for (Effect<Enemy> effect : activeEffects)
        {
            if (effect instanceof  IDefensiveEffect  iDefensiveEffect)
            {
                finalDamage = iDefensiveEffect.applyDefense(finalDamage);
            }
        }
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

        int healthincrease = (int) (this.maxHealth * health_scaling * level);
        this.maxHealth = maxHealth + healthincrease;

        int damageincrease = (int) (this.baseDamage * damage_scaling * level );
        this.baseDamage = baseDamage + damageincrease ;

        
    }
    
    @Override
    public String applyTurnEffects() {
        if (activeEffects.isEmpty())
        {
            return "";
        }

    StringBuilder effectsResult = new StringBuilder();
    Iterator<Effect<Enemy>> iterator = activeEffects.iterator();
    
    while (iterator.hasNext()) {
        Effect<Enemy> effect = iterator.next();
        String result = effect.apply(this);
        
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
        return this.name + " attacks " + target.getName() + " for " + this.baseDamage + " damage.";
    }

    public void addEffect(Effect<Enemy> effect) {
        activeEffects.add(effect);
    }


    public boolean hasEffect(String effectName) {
        for (Effect<Enemy> effect : activeEffects) {
            if (effect.getName().equals(effectName)) {
            return true;
            }
        }
        return false;
    }

    public void removeEffect(String effectName) {
        Iterator<Effect<Enemy>> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect<Enemy> effect = iterator.next();
            if (effect.getName().equals(effectName)) {
                iterator.remove();
                break;
                }
            }
        }


    public void removeEffectsOfType(Class<?> effectType) {
        Iterator<Effect<Enemy>> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect<Enemy> effect = iterator.next();
            if (effectType.isInstance(effect)) {
                iterator.remove();
            }
        }
    }

    public List<Effect<Enemy>> getActiveEffects() 
    {
        return Collections.unmodifiableList(activeEffects);
    }
}



