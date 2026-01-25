package controller;

import exceptions.InvalidMoveException;
import model.Inventory;
import model.Iwarrior;
import model.Player;
import model.Weapon;


//AttackAction is a helper class that handles weapon attack logic.
//this includes damage calculation, weapon durability management, and weapon breaking.
 
public final class AttackAction {//only static methods, and there is no logical reason to inherit from them.
    //do not forget it is a designe rule not a language rule 
  
    private AttackAction()
    {
        throw new AssertionError("No instances allowed");
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
                       " damage! and " + effectivenessMessage;
        
       
        if (weapon.getDurability() <= 0) {
            String brokenWeaponName = weapon.getName();
            inventory.removeItem(weapon);
            attacker.setEquippedWeapon(null);
            result += "\nYour " + brokenWeaponName + " broke!";
        }
        
        return result;
    }
}

/**
 * I used a helper class (AttackAction) to handle the attack logic because:
 * 
 * 1. Separation of Concerns: The attack process involves multiple steps (damage calculation, weapon durability, inventory updates, and messaging). By moving this logic to a helper class, I keep the Player and Weapon classes focused on their main responsibilities.
 * 
 * 2. Reusability: AttackAction can be reused for different types of attacks or extended for other characters (e.g., enemies or NPCs) without duplicating code.
 * 
 * 3. Maintainability: If the attack logic changes (e.g., adding critical hits, special effects, or new weapon types), I only need to update one place, making the code easier to maintain and less error-prone.
 * 
 * 4. Testability: Having attack logic in a separate class makes it easier to write unit tests for attack scenarios, since the logic is isolated from the rest of the Player or Weapon code.
 * 
 * 5. Clean Code: This approach keeps the Player class simpler and more readable, as it delegates complex actions to specialized classes.
 * 
 * In summary, using AttackAction follows good software engineering practices by promoting modularity, reusability, and clean separation of responsibilities.
 */

//Attack behavior is uniform across all weapons, so I do not need polymorphic objects