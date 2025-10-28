package model;

import controller.RuleEngine;
import utils.DiceRoller;


public class Bean extends Player {
    public Bean() {
        // A rebellious princess, surprisingly tough.
        super("Bean", 100);
        // Bean is tough and has a natural resistance to poison.
        this.ruleEngine.setRule(RuleEngine.POISON_RESISTANCE, 0.5);
    }

    /**
     * Bean's special ability: She's tough and can recover a small amount of health in a pinch.
     * @param enemy The enemy to attack (not used for this ability).
     * @param dice The dice roller for game randomness (not used for this ability).
     * @return A string describing the result of the ability.
     */
    @Override
    public String useSpecialAbility(Enemy enemy, DiceRoller dice) {
        int healAmount = 15;
        this.heal(healAmount);
        // Add a defensive stance effect that lasts for 1 turn (the enemy's immediate counter-attack)
        this.addEffect(new DefensiveStanceEffect(1));
        putSpecialAbilityOnCooldown(); // Put the ability on cooldown
        return "Bean scoffs, 'Is that all you've got?' She recovers " + healAmount + " health and braces for the next attack.";
    }
}