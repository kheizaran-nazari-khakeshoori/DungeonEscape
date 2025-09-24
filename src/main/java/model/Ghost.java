package model;

import utils.DiceRoller;

public class Ghost extends Enemy {

    public Ghost() {
        // Name, Health, Base Damage, Image Path
        super("Ghost", 40, 12, "images/enemies/Ghost.png");

        // Ghosts are resistant to physical attacks but weak to holy magic.
        resistances.put(DamageType.SLASHING, 0.5); // Takes half damage
        resistances.put(DamageType.PIERCING, 0.5); // Takes half damage

        resistances.put(DamageType.HOLY, 2.0);      // Takes double damage
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        return this.name + " phases through you, dealing " + this.baseDamage + " chilling damage.";
    }

    @Override
    public String getHint() {
        return "Hint: It appears spectral and translucent. Physical attacks might be less effective.";
    }
}