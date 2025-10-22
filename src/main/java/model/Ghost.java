package model;
import utils.DiceRoller;
//demonstrting inheritance by by reusing all common behavior 
public class Ghost extends Enemy {
    public Ghost() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Ghost", 40, 12, 25, "images/enemies/Ghost.png", DamageType.MAGIC);

        // Ghosts are resistant to physical attacks and weak to holy magic.
        addResistance(DamageType.SLASHING, 0.5);//
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

// using DRY principle
//methodes are overriden only if the behavior itself needs to change not the data 
//in attack ichamged the message but in weakness i only add data to the map not changing the methodes as you can see 

