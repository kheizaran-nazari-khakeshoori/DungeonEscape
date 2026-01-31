package model;

import controller.RuleEngine;
import exceptions.InvalidMoveException;

public class Elfo extends Player {
    public Elfo() {
        super("Elfo", 75);
    
       getRuleEngine().setRule(RuleEngine.getFleeChance(), 0.75); 
       getRuleEngine().setRule(RuleEngine.getTrapDisarmChance(),0.75);
    }

  

    @Override
    public String useSpecialAbility(Iwarrior target) throws InvalidMoveException {
        if (getEquippedWeapon() == null) {
            throw new InvalidMoveException("you can not attack without weapon");
        }

        int bonusDamage = 10;
        String effectiveness = target.takeDamage(bonusDamage, DamageType.PIERCING); 

        putSpecialAbilityOnCooldown(); 

        return "Elfo takes a moment to aim carefully... and lands a precise shot for " +
               bonusDamage + " bonus damage! " + effectiveness;
    }

    @Override
    public String getImagePath() {
        return "images/players/Elfo.png";
    }

}