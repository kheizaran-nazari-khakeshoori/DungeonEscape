package model;
import utils.DiceRoller;

public class Ghost extends Enemy {
    public Ghost() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Ghost", 40, 12, 25, "images/enemies/Ghost.png", DamageType.MAGIC);

        // Ghosts are resistant to physical attacks and weak to holy magic.
        addResistance(DamageType.SLASHING, 0.5);
        addResistance(DamageType.PIERCING, 0.5);
        addWeakness(DamageType.HOLY, 2.0);
    }

    @Override
    public String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException {
        target.takeDamage(this.baseDamage);
        return this.name + " phases through you, dealing " + this.baseDamage + " chilling damage.";
    }

    @Override
    public String getHint() {
        return "Hint: It appears spectral and translucent. Physical attacks might be less effective.";
    }
}