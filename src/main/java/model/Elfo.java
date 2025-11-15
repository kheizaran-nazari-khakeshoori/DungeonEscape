package model;

import controller.RuleEngine;

public class Elfo extends Player {
    public Elfo() {
        super("Elfo", 75);
    
       getRuleEngine().setRule(RuleEngine.FLEE_CHANCE, 0.75); 
    }

    @Override
    public double getTrapDisarmChance() { 
        return 0.66; 
    }

    @Override
    public String useSpecialAbility(Iwarrior target) {
        if (getEquippedWeapon() == null) {
            return "Elfo tries to aim, but has nothing to shoot with!";
        }

        int bonusDamage = 10;
        String effectiveness = target.takeDamage(bonusDamage, DamageType.PIERCING); 

        putSpecialAbilityOnCooldown(); 

        return "Elfo takes a moment to aim carefully... and lands a precise shot for " +
               bonusDamage + " bonus damage! " + effectiveness;
    }
}