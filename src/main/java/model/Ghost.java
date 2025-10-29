package model;
import utils.DiceRoller;

public class Ghost extends Enemy {
    public Ghost() {
        super("Ghost", 40, 12, 25, "images/enemies/Ghost.png", DamageType.MAGIC);

        
        addResistance(DamageType.SLASHING, 0.5);
        addResistance(DamageType.PIERCING, 0.5);
        addWeakness(DamageType.HOLY, 2.0);
    }

    @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
        target.takeDamage(this.baseDamage);
        return this.name + " phases through you, dealing " + this.baseDamage + " chilling damage.";
    }

    @Override
    public String getHint() {
        return "Hint: Physical attacks might be less effective.";
    }
}


