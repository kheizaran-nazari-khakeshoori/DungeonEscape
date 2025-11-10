package model;

public class SkeletonWarrior extends Enemy {
    public SkeletonWarrior() {
        
        super("Skeleton Warrior", 50, 10, 30, "images/enemies/SkeletonWarrior.png", model.DamageType.PHYSICAL);


        addWeakness(DamageType.BLUNT, 1.5);
        addResistance(DamageType.PIERCING, 0.75);
    }

   @Override
    protected String getAttackMessage() {
        return this.name + " can kill you by " + this.baseDamage + " damage!";
    }



    @Override
    public String getHint() {
        return "Hint: A clattering skeleton. Piercing weapons might not be very effective against its bony frame.";
    }
}