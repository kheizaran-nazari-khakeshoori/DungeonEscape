package model;

import java.util.HashMap;
import java.util.Map;

import utils.DiceRoller;

public abstract class Enemy {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int baseDamage;
    protected String imagePath;
    // Map of DamageType to a multiplier (e.g., 2.0 for weakness, 0.5 for resistance)
    protected Map<DamageType, Double> resistances = new HashMap<>();

    public Enemy(String name, int health, int baseDamage, String imagePath) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.baseDamage = baseDamage;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public String getImagePath() { return imagePath; }
    public boolean isAlive() { return health > 0; }

    /**
     * Applies damage to the enemy, considering its resistances and weaknesses.
     * @param amount The base damage amount.
     * @param type The type of damage being dealt.
     * @return A string describing the effectiveness of the attack.
     */
    public String takeDamage(int amount, DamageType type) {
        double multiplier = resistances.getOrDefault(type, 1.0);
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
     * @param level The number of times this enemy has been encountered before.
     */
    public void strengthen(int level) {
        // Increase health by 20% and damage by 10% for each previous encounter
        this.maxHealth = (int) (this.maxHealth * (1 + 0.2 * level));
        this.health = this.maxHealth;
        this.baseDamage = (int) (this.baseDamage * (1 + 0.1 * level));
    }

    // Each enemy must implement its own attack logic.
    public abstract String attack(Player player, DiceRoller dice);

    // Each enemy can provide a hint about its nature.
    public abstract String getHint();
}