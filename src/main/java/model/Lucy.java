package model;

import controller.RuleEngine;


public class Lucy extends Player {
    public Lucy() {
        super("Lucy", 85);
       
        this.ruleEngine.setRule(RuleEngine.FLEE_CHANCE, 0.25); 
    }

    @Override 
    public double getTrapDisarmChance() {
        return 0.20; 
    }

    
    @Override
    public String useSpecialAbility(Iwarrior target) {
        int damage = 35;
        int penalty = 10;

        String effectiveness = target.takeDamage(damage, DamageType.PHYSICAL);
        this.takeDamage(penalty); 
        putSpecialAbilityOnCooldown();
        return "Lucy unleashes a reckless flurry, dealing a massive " + damage + " damage! " + effectiveness + "\nShe takes " + penalty + " damage from the exertion.";
    }
}
