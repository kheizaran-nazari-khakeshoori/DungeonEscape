package model;

import controller.RuleEngine;
import utils.DiceRoller;

public class Elfo extends Player {
    public Elfo() {
        // An optimistic elf, not built for combat but surprisingly resilient.
        super("Elfo", 75);
        // Elfo is nimble and better at fleeing. We can customize his rules.
        this.ruleEngine.setRule(RuleEngine.FLEE_CHANCE, 0.75); // 75% flee chance for Elfo
    }//using the composed ruleengine defining uniqness 

    /**
     * Elfo is nimble and has a better chance of avoiding traps.
     */
    @Override// polymorphism cause you call the same method but get the different behavior
    public double getTrapDisarmChance() { 
        return 0.66; // Elfo has a 66% chance to succeed.
    }

    /**
     * Elfo's special ability: A carefully aimed shot that deals bonus damage.
     * @param enemy The enemy to attack.
     * @param dice The dice roller for game randomness.
     * @return A string describing the result of the attack.
     */
    @Override
    public String useSpecialAbility(Enemy enemy, DiceRoller dice) {
        if (getEquippedWeapon() == null) {//checking for having weapon 
            return "Elfo tries to aim, but has nothing to shoot with!";
        }

        int bonusDamage = 10;
        String effectiveness = enemy.takeDamage(bonusDamage, DamageType.PIERCING); // Apply bonus damage directly

        putSpecialAbilityOnCooldown(); // Put the ability on cooldown

        return "Elfo takes a moment to aim carefully... and lands a precise shot for " +
               bonusDamage + " bonus damage! " + effectiveness;
    }
}