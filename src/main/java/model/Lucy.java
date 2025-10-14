package model;

import controller.RuleEngine;
import utils.DiceRoller;

public class Lucy extends Player {
    public Lucy() {
        // A character who hits hard but is a bit of a glass cannon.
        super("Lucy", 85);
        // Lucy is not great at fleeing.
        this.ruleEngine.setRule(RuleEngine.FLEE_CHANCE, 0.25); // 25% flee chance
    }

    @Override
    public double getTrapDisarmChance() {
        return 0.20; // Lucy is not the most careful.
    }

    /**
     * Lucy's special ability: A powerful, reckless attack that deals massive damage
     * but also causes some recoil damage to herself.
     */
    @Override
    public String useSpecialAbility(Enemy enemy, DiceRoller dice) {
        int damageDealt = 35;
        int recoilDamage = 15;

        String effectiveness = enemy.takeDamage(damageDealt, DamageType.PHYSICAL);
        this.takeDamage(recoilDamage); // Lucy takes recoil damage
        putSpecialAbilityOnCooldown();
        return "Lucy unleashes a reckless flurry, dealing a massive " + damageDealt + " damage! " + effectiveness + "\nShe takes " + recoilDamage + " damage from the exertion.";
    }
}