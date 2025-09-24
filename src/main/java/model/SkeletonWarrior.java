package model;

import utils.DiceRoller;

public class SkeletonWarrior extends Enemy {

    public SkeletonWarrior() {
        // Name, Health, Base Damage, Image Path
        super("Skeleton Warrior", 50, 10, "images/enemies/SkeletonWarrior.png");


        // Skeletons are often weak to blunt damage and resistant to piercing.
        resistances.put(DamageType.BLUNT, 1.5);
        resistances.put(DamageType.PIERCING, 0.75);
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        return this.name + " strikes you with its rusty sword for " + this.baseDamage + " damage.";
    }

    @Override
    public String getHint() {
        return "Hint: A clattering skeleton. Piercing weapons might not be very effective against its bony frame.";
    }
}