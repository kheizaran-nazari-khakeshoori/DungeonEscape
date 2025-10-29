package model;
import utils.DiceRoller;

public class ShadowAssassin extends Enemy {
     public ShadowAssassin() {
        
        super("Shadow Assassin", 55, 20, 40, "images/enemies/ShadowAssassin.png", DamageType.SHADOW);
    }


    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
       
        int variation= this.baseDamage + dice.roll(5) - 2; 
        return this.name + " strikes from the shadows for " + variation + " variation!";
    }

    @Override
    public String getHint() {
        return "Hint: It moves with blinding speed. A quick, decisive blow might be your only chance.";
    }
}