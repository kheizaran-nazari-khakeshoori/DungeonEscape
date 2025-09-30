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

        // 25% chance to apply its unique, weaker poison.
        if (dice.roll(4) == 1) { // A 1-in-4 chance
            player.addEffect(new GoblinPoisonEffect(2, 2)); // 2 damage for 2 turns
            result += "\nIts rusty blade leaves a festering wound. You've been poisoned!";
        }
        return result;
    }

    @Override
    public String getHint() {
        return "Hint: This creature looks straightforward. A standard weapon should suffice.";
    }
}