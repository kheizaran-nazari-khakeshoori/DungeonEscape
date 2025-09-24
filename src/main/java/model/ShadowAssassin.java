package model;

import utils.DiceRoller;

public class ShadowAssassin extends Enemy {

    public ShadowAssassin() {

        // Name, Health, Base Damage, Image Path
        super("Shadow Assassin", 55, 20, "images/enemies/ShadowAssassin.png");
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        // Has a slightly variable but high damage output
        int damage = this.baseDamage + dice.roll(5) - 2; // Damage between 18-23
        player.takeDamage(damage);
        return this.name + " strikes from the shadows for " + damage + " damage!";
    }

    @Override
    public String getHint() {
        return "Hint: It moves with blinding speed. A quick, decisive blow might be your only chance.";
    }
}