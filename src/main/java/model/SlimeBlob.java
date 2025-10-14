package model;
import utils.DiceRoller;

public class SlimeBlob extends Enemy {
    public SlimeBlob() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Slime Blob", 60, 9, 20, "images/enemies/SlimeBlob.png", model.DamageType.ACID);

        // Slimes are resistant to sharp things but weak to fire
        resistances.put(DamageType.SLASHING, 0.5);
        resistances.put(DamageType.PIERCING, 0.5);
        resistances.put(DamageType.FIRE, 1.5);
    }

    @Override
    public String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException {
       target.takeDamage(this.baseDamage);
        return this.name + " engulfs you with a pseudopod, dealing " + this.baseDamage + " acidic damage.";
    }


    @Override
    public String getHint() {
        return "Hint: Its gelatinous body seems to absorb physical blows. Maybe something else would work better?";
    }
}