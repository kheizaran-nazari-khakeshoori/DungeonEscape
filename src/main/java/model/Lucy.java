package model;

import controller.RuleEngine;


public class Lucy extends Player {
    public Lucy() {
        super("Lucy",85);
       
        getRuleEngine().setRule(RuleEngine.getFleeChance(), 0.25); 
        getRuleEngine().setRule(RuleEngine.getTrapDisarmChance(),0.33);
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
