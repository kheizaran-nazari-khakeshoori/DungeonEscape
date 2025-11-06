package controller;

import exceptions.InvalidMoveException;
import model.Inventory;
import model.Iwarrior;
import model.Player;
import model.Weapon;

/**
 * AttackAction is a helper class that handles weapon attack logic.
 * This includes damage calculation, weapon durability management, and weapon breaking.
 */
public final class AttackAction {
    
    // Private constructor to prevent instantiation (utility class)
    private AttackAction() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Performs a weapon attack from attacker to target
     * 
     * @param attacker The player performing the attack
     * @param target The target being attacked (Enemy or another Player)
     * @param weapon The weapon being used
     * @param inventory The attacker's inventory (to remove broken weapon)
     * @return Combat message describing what happened
     * @throws InvalidMoveException if no weapon is equipped
     */
    public static String performWeaponAttack(
            Player attacker,
            Iwarrior target,
            Weapon weapon,
            Inventory inventory) throws InvalidMoveException {
        
        // Check if weapon exists
        if (weapon == null) {
            throw new InvalidMoveException("You have no weapon equipped to attack!");
        }
        
        // Get weapon damage
        int baseDamage = weapon.getDamage();
        
        // Apply damage to target (target handles effectiveness message)
        String effectivenessMessage = target.takeDamage(baseDamage, weapon.getDamageType());
        
        // Decrease weapon durability
        weapon.decreaseDurability();
        
        // Build result message
        String result = attacker.getName() + " attacks " + target.getName() + 
                       " with " + weapon.getName() + " for " + baseDamage + 
                       " damage! " + effectivenessMessage;
        
        // Handle weapon breaking
        if (weapon.getDurability() <= 0) {
            String brokenWeaponName = weapon.getName();
            inventory.removeItem(weapon);
            attacker.setEquippedWeapon(null);
            result += "\nYour " + brokenWeaponName + " broke!";
        }
        
        return result;
    }
}
