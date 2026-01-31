package model;

import controller.RuleEngine;
import exceptions.InvalidMoveException;


public class Lucy extends Player {
    public Lucy() {
        super("Lucy",85);
       
        //"This follows the Strategy pattern concept - I'm parameterizing behavior rather than hardcoding it.
        getRuleEngine().setRule(RuleEngine.getFleeChance(), 0.25); // character-specific state 
        getRuleEngine().setRule(RuleEngine.getTrapDisarmChance(),0.33);
    }

    

    
    @Override
    public String useSpecialAbility(Iwarrior target) throws InvalidMoveException{
        if (getEquippedWeapon() == null) {
           throw new InvalidMoveException("you can not attack without weapon");
        }
        int damage = 35;
        int penalty = 10;

        String effectiveness = target.takeDamage(damage, DamageType.PHYSICAL);
        this.takeDamage(penalty); 
        putSpecialAbilityOnCooldown();
        return "Lucy unleashes a reckless flurry, dealing a massive " + damage + " damage! " + effectiveness + "\nShe takes " + penalty + " damage from the exertion.";
    }

    @Override
    public String getImagePath() {
        return "images/players/Lucy.png";
    }
}
