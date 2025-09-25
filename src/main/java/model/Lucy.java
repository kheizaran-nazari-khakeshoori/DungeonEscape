package model;

import utils.DiceRoller;

public class Lucy extends Player {
    public Lucy() {
        // Bean's personal demon. Fragile but packs a punch. Do it, do it, do it!
        super("Lucy", 66);
    }

    /**
     * Lucy's special ability: A high-risk, high-reward attack that deals
     * significant bonus damage but also damages Lucy.
     * @param enemy The enemy to attack.
     * @param dice The dice roller for game randomness.
     * @return A string describing the result of the attack.
     */
    @Override
    public String useSpecialAbility(Enemy enemy, DiceRoller dice) {
        int selfDamage = 10;
        int bonusDamage = 20;
        this.takeDamage(selfDamage);
        putSpecialAbilityOnCooldown(); // Put the ability on cooldown
        return "Lucy screams 'DO IT!' and recklessly attacks!\n" + this.attack(enemy, bonusDamage, dice);
    }
}