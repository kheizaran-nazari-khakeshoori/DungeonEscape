package model;

import utils.DiceRoller;

public class MimicChest extends Enemy {

    public MimicChest() {
        // Name, Health, Base Damage, Image Path
        super("Mimic Chest", 70, 18, "images/enemies/MimicChest.png");


        // Mimics are tough, maybe slightly resistant to being pierced
        resistances.put(DamageType.PIERCING, 0.8);
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        return this.name + " reveals its teeth and chomps down on you for " + this.baseDamage + " damage!";
    }

    @Override
    public String getHint() {
        return "Hint: It looks like a treasure chest... but something feels off.";
    }
}