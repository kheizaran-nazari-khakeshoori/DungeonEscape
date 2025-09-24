package controller;

/**
 * A centralized class to hold and manage game rules and constants.
 * This makes the game easily customizable, as all key values are in one place.
 * In a more advanced implementation, these values could be loaded from a configuration file.
 */
public class RuleEngine {

    // --- Encounter Rules ---
    public double getEnemyChance() { return 0.7; } // 70% chance for an enemy
    public double getTrapChance() { return 0.3; } // 30% chance for a trap

    // --- Combat Rules ---
    public double getFleeChance() { return 0.5; } // 50% chance to flee successfully

    // --- Enemy Scaling Rules ---
    /**
     * @return The multiplier for an enemy's health increase per level. (e.g., 0.2 = +20%)
     */
    public double getEnemyHealthScaling() { return 0.2; }

    /**
     * @return The multiplier for an enemy's damage increase per level. (e.g., 0.1 = +10%)
     */
    public double getEnemyDamageScaling() { return 0.1; }

    public RuleEngine() {
        // In the future, this constructor could load rules from a file.
    }
}
