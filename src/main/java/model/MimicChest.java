package model;
import utils.DiceRoller;

public class MimicChest extends Enemy {
   public MimicChest() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Mimic Chest", 70, 18, 80, "images/enemies/MimicChest.png", DamageType.PHYSICAL);

        // Mimics are tough, maybe slightly resistant to piercing
        addResistance(DamageType.PIERCING, 0.8);
    }

    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
       target.takeDamage(this.baseDamage);
        return this.name + " reveals its teeth and chomps down on you for " + this.baseDamage + " damage!";
    }


    @Override
    public String getHint() {
        return "Hint: It looks like a treasure chest... but something feels off.";
    }
}