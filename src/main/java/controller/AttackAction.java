package controller;

import exceptions.InvalidMoveException;
import model.Inventory;
import model.Iwarrior;
import model.Player;
import model.Weapon;


//AttackAction is a helper class that handles weapon attack logic.
//this includes damage calculation, weapon durability management, and weapon breaking.
 
public final class AttackAction {
  
    private AttackAction() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    
    public static String performWeaponAttack(Player attacker,Iwarrior target,Weapon weapon,Inventory inventory) throws InvalidMoveException {
        
        if (weapon == null) {
            throw new InvalidMoveException("You have no weapon equipped to attack!");
        }
          
        int baseDamage = weapon.getDamage();
          
        String effectivenessMessage = target.takeDamage(baseDamage, weapon.getDamageType());
        
        weapon.decreaseDurability();
        
        String result = attacker.getName() + " attacks " + target.getName() + 
                       " with " + weapon.getName() + " for " + baseDamage + 
                       " damage! " + effectivenessMessage;
        
       
        if (weapon.getDurability() <= 0) {
            String brokenWeaponName = weapon.getName();
            inventory.removeItem(weapon);
            attacker.setEquippedWeapon(null);
            result += "\nYour " + brokenWeaponName + " broke!";
        }
        
        return result;
    }
}
