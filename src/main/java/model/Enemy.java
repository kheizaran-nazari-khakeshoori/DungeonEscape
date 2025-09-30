package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import controller.RuleEngine;
import utils.DiceRoller;

public abstract class Enemy {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int baseDamage;
    protected String imagePath;
    protected int goldValue; // How much gold the enemy is worth
    // Map of DamageType to a multiplier (e.g., 2.0 for weakness, 0.5 for resistance)
    protected Map<DamageType, Double> resistances = new HashMap<>();
    protected List<Supplier<Item>> lootTable = new ArrayList<>();

    public Enemy(String name, int health, int baseDamage, String imagePath) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.baseDamage = baseDamage;
        this.goldValue = baseDamage * 2; // The gold value is a function of damage.
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public String getImagePath() { return imagePath; }

    public int getGoldValue() { return goldValue; }

    public boolean isAlive() { return health > 0; }

    /**
     * Applies damage to the enemy, considering its resistances and weaknesses.
     * @param amount The base damage amount.
     * @param type The type of damage being dealt.
     * @return A string describing the effectiveness of the attack.
     */
    public String takeDamage(int amount, DamageType type) {
        double multiplier = resistances.getOrDefault(type, 1.0);

        // COERCION: The 'int' (amount) is implicitly converted to a 'double'
        // to be multiplied by the 'double' (multiplier).
        int finalDamage = (int) (amount * multiplier);
        this.health -= finalDamage;
        if (this.health < 0) this.health = 0;

        if (multiplier > 1.5) {
            return "It's super effective!";
        } else if (multiplier < 0.75) {
            return "It's not very effective...";
        }
        return "";
    }

    /**
     * Increases the enemy's stats based on an encounter level.
     * Now uses the RuleEngine for scaling values.
     * @param level The number of times this enemy has been encountered before.
     * @param rules The rule engine providing scaling factors.
     */
    public void strengthen(int level, RuleEngine rules) {
        this.maxHealth = (int) (this.maxHealth * (1 + rules.getRule(RuleEngine.ENEMY_HEALTH_SCALING) * level));
        this.health = this.maxHealth;
        this.baseDamage = (int) (this.baseDamage * (1 + rules.getRule(RuleEngine.ENEMY_DAMAGE_SCALING) * level));
    }

    // Each enemy must implement its own attack logic.
    public abstract String attack(Player player, DiceRoller dice);

    // Each enemy can provide a hint about its nature.
    public abstract String getHint();

    /**
     * Generates a loot item based on the enemy's loot table and randomness.
     * @param dice The dice roller to determine if loot drops.
     * @return An Item if one is dropped, otherwise null.
     */
    public Item dropLoot(DiceRoller dice) {
        // Example: 33% chance to drop an item from the loot table.
        if (!lootTable.isEmpty() && dice.roll(3) == 1) {
            int lootIndex = dice.getRandom().nextInt(lootTable.size());
            return lootTable.get(lootIndex).get();
        }
        return null;
    }
}