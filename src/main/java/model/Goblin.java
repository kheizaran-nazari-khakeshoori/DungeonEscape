package model;

import utils.DiceRoller;

public class Goblin extends Enemy {

    public Goblin() {
        // Name, Health, Base Damage, Image Path
        super("Goblin", 30, 8, "images/enemies/Goblin.png");
        // Goblins have no special resistances or weaknesses.

    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        String result = this.name + " attacks you for " + this.baseDamage + " damage.";

        // 25% chance to apply poison
        if (dice.roll(4) == 1) { // A 1-in-4 chance
            player.addEffect(new PoisonEffect(3, 3)); // 3 damage for 3 turns
            result += "\nYou have been poisoned!";
        }
        return result;
    }

    @Override
    public String getHint() {
        return "Hint: This creature looks straightforward. A standard weapon should suffice.";
    }
}