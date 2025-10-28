package model;
import utils.DiceRoller;

public class ShadowAssassin extends Enemy {
     public ShadowAssassin() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Shadow Assassin", 55, 20, 40, "images/enemies/ShadowAssassin.png", DamageType.SHADOW);
    }


    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
        // Has a slightly variable but high damage output
        int damage = this.baseDamage + dice.roll(5) - 2; // Damage between 18-23
        target.takeDamage(damage);
        return this.name + " strikes from the shadows for " + damage + " damage!";
    }

    @Override
    public String getHint() {
        return "Hint: It moves with blinding speed. A quick, decisive blow might be your only chance.";
    }
}