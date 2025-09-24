package model;

import utils.DiceRoller;

public class SlimeBlob extends Enemy {

    public SlimeBlob() {
        // Name, Health, Base Damage, Image Path
        super("Slime Blob", 60, 9, "images/enemies/SlimeBlob.png");


        // Slimes are resistant to sharp things but weak to fire/magic
        resistances.put(DamageType.SLASHING, 0.5);
        resistances.put(DamageType.PIERCING, 0.5);
        resistances.put(DamageType.FIRE, 1.5);
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        return this.name + " engulfs you with a pseudopod, dealing " + this.baseDamage + " acidic damage.";
    }

    @Override
    public String getHint() {
        return "Hint: Its gelatinous body seems to absorb physical blows. Maybe something else would work better?";
    }
}