package model;

import java.util.List;

import controller.AttackAction;
import controller.EffectManager;
import controller.RuleEngine; 
import exceptions.InvalidMoveException;
import utils.DiceRoller;
//modeling an entity 
//this class manages the states and the behaviors that a player must have
//final object → reference fixed, contents can change
//final primitive (int, double) → value cannot change, ever
public abstract class Player implements Iwarrior, IEffectable<Player> {
    private static final int COOLDOWN_TURNS = 3;//one copy 

    private final String name;
    private final int maxHealth;
    private int health;
    private int gold;
    private final Inventory inventory;//only one inventory per player
    private Weapon equippedWeapon;
    private final EffectManager<Player> effectManager;
    private final RuleEngine ruleEngine; 
    private int currentSpecialAbilityCooldown;
    

    public Player(String name, int maxHealth) {
    
       

        this.name = name;
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.inventory = new Inventory(); 
        this.effectManager = new EffectManager<>();
        this.equippedWeapon = null;
        this.gold = 0; 
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
        takeDamage(amount,null);//no-type version should still use the complete damage-processing logic.
    }

    // In Java, overloading is determined by the parameter list (number, types, and order of parameters), NOT the return type. So yes, having different return types (void vs String) is perfectly valid as long as the parameters are different. The compiler chooses which method to call based on the arguments you pass


    @Override
    public String takeDamage(int amount, DamageType type) {
        int finalDamage = amount;
        String message = "";
        
        
        for (Effect<Player> effect : effectManager.getActiveEffects()) {
            int reducedDamage = effect.modifyIncomingDamage(finalDamage);
            if (reducedDamage < finalDamage) {
                message = " (Reduced by " + effect.getName() + "!)";
                finalDamage = reducedDamage;
            }
        }
        
        this.health = health - finalDamage;
        if(this.health < 0 ) this.health = 0;
        return message; 
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
    public void removeEffectsOfType(Class<? extends Effect<Player>> effectType) {
        effectManager.removeEffectsOfType(effectType);
    }
    //= wildcard meaning "any type that is Effect<Player> or a subclass of it"
    //So you could pass: PoisonEffect.class, InvisibilityEffect.class, etc.
 
    @Override
    public List<Effect<Player>> getActiveEffects() {
        return effectManager.getActiveEffects();
    }

    

    
    public double getPoisonResistance() {
        return ruleEngine.getRule(RuleEngine.getPoisonResistance());
    }

    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

   
    public double getTrapDisarmChance() {
        return ruleEngine.getRule(RuleEngine.getTrapDisarmChance());
    }
    
    public abstract String useSpecialAbility(Iwarrior target);

    
    public boolean isSpecialAbilityReady() {
        return currentSpecialAbilityCooldown == 0;
    }

    
    public void tickCooldowns() {
        if (currentSpecialAbilityCooldown > 0) {
            currentSpecialAbilityCooldown--;
        }
    }

    
    protected void putSpecialAbilityOnCooldown() { this.currentSpecialAbilityCooldown = COOLDOWN_TURNS; } 
    //Should you change it? Not necessarily! It depends on your design: Current design is fine if you trust classes in the model package If you want stricter encapsulation, you could make it private and force subclasses to only use it indirectly The current protected design is a reasonable choice for this game
}
