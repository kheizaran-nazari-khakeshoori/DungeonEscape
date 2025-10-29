package model;

import utils.DiceRoller;

public class StoneMan extends Enemy {
    public StoneMan() {
        
        super("StoneMan", 80, 15, 50, "images/enemies/StoneMan.png", DamageType.BLUNT);


        addResistance(DamageType.SLASHING, 0.5);
        addResistance(DamageType.PIERCING, 0.25);
        addWeakness(DamageType.BLUNT, 1.75);
    }

    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
       target.takeDamage(this.baseDamage);
        return this.name + " slams you for " + this.baseDamage + " heavy damage.";
    }


    @Override
    public String getHint() {
        return "Hint: Its body is made of solid rock. Sharp weapons may glance off it.";
    }
}