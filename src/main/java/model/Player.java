package model;

import controller.AttackAction;
import controller.EffectManager;
import controller.RuleEngine; 
import exceptions.InvalidMoveException;
import utils.DiceRoller;

public abstract class Player implements Iwarrior,IOperation_on_Effect<Player>{
    private final String name;
    private final int maxHealth;
    private int health;
    private final Inventory inventory;
    
    private Weapon equippedWeapon;
    private int gold; 
    private final EffectManager<Player> effectManager;
    
    protected int specialAbilityCooldownTurns;
    protected int currentSpecialAbilityCooldown;
    protected final RuleEngine ruleEngine; 

    public Player(String name, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.inventory = new Inventory(); 
        this.effectManager = new EffectManager<>();
        this.equippedWeapon = null;
        this.gold = 0; 
        this.specialAbilityCooldownTurns = 3; 
        this.currentSpecialAbilityCooldown = 0; 
        this.ruleEngine = new RuleEngine(); 
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
        takeDamage(amount,null);
    }


    @Override
    public String takeDamage(int amount, DamageType type) {
        int finalDamage = amount;
        for (Effect<Player> effect : effectManager.getActiveEffects())
        {
            if(effect instanceof IDefensiveEffect iDefensiveEffect)
            {
                finalDamage = iDefensiveEffect.applyDefense(finalDamage);
            }
        }
        this.health = health - finalDamage ;
        if(this.health < 0 ) this.health = 0;
        return ""; 
    }

    public void heal(int amount) {
        this.health = health + amount;
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
        return AttackAction.performWeaponAttack(this, target, equippedWeapon, inventory);
    }
    @Override
    public void addEffect(Effect<Player> effect) { 
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

    @Override
    public String applyTurnEffects() { 
        return effectManager.applyAllEffects(this);
    }

    
    public double getTrapDisarmChance() {
        return 0.33; 
    }

    
    public double getPoisonResistance() {
        return ruleEngine.getRule(RuleEngine.POISON_RESISTANCE);
    }

    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

   
    public abstract String useSpecialAbility(Enemy enemy);

    
    public boolean isSpecialAbilityReady() {
        return currentSpecialAbilityCooldown == 0;
    }

    
    public void tickCooldowns() {
        if (currentSpecialAbilityCooldown > 0) {
            currentSpecialAbilityCooldown--;
        }
    }

    public int getCurrentSpecialAbilityCooldown() {
        return currentSpecialAbilityCooldown;
    }

    
    protected void putSpecialAbilityOnCooldown() { this.currentSpecialAbilityCooldown = this.specialAbilityCooldownTurns; } 
}
//
