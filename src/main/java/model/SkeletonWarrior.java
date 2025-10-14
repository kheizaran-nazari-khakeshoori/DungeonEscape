package model;
import utils.DiceRoller;

public class SkeletonWarrior extends Enemy {
    public SkeletonWarrior() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Skeleton Warrior", 50, 10, 30, "images/enemies/SkeletonWarrior.png", model.DamageType.PHYSICAL);

        // Skeletons are often weak to blunt damage and resistant to piercing
        weaknesses.put(DamageType.BLUNT, 1.5); // Weaknesses go in the weaknesses map
        resistances.put(DamageType.PIERCING, 0.75);
    }

    @Override
    public String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException {
       target.takeDamage(this.baseDamage);
        return this.name + " strikes you with its rusty sword for " + this.baseDamage + " damage.";
    }


    @Override
    public String getHint() {
        return "Hint: A clattering skeleton. Piercing weapons might not be very effective against its bony frame.";
    }
}