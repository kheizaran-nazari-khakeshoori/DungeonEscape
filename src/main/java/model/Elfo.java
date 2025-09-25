package model;

import utils.DiceRoller;

public class Elfo extends Player {
    public Elfo() {
        // An optimistic elf, not built for combat but surprisingly resilient.
        super("Elfo", 75);
    }

    /**
     * Elfo is nimble and has a better chance of avoiding traps.
     */
    @Override
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
        int bonusDamage = 10;
        // In a more complex system, this could bypass enemy evasion. For now, it's a reliable damage boost.
        putSpecialAbilityOnCooldown(); // Put the ability on cooldown
        return "Elfo takes a moment to aim carefully...\n" + this.attack(enemy, bonusDamage, dice);
    }
}