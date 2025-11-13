package model;

import controller.RuleEngine;


public class Bean extends Player {
    public Bean() {
        
        super("Bean", 100);
        
        this.ruleEngine.setRule(RuleEngine.POISON_RESISTANCE, 0.5);
    }

   
    @Override
    public String useSpecialAbility(Iwarrior target) {
        // Note: Bean's ability is self-targeted (defensive), doesn't use target parameter
        int healAmount = 15;
        this.heal(healAmount);
        this.addEffect(new Defensive_type_Effect(1));
        putSpecialAbilityOnCooldown(); 
        return "Bean scoffs, 'Is that all you've got?' She recovers " + healAmount + " health and braces for the next attack.";
    }
}