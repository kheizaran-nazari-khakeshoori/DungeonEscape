package model;

import utils.DiceRoller;

public class PoisonSpider extends Enemy {

    public PoisonSpider() {
        // Name, Health, Base Damage, Image Path
        super("Poison Spider", 35, 6, "images/enemies/PoisonSpider.png");
    }

    @Override
    public String attack(Player player, DiceRoller dice) {
        player.takeDamage(this.baseDamage);
        String result = this.name + " bites you for " + this.baseDamage + " damage.";

        // 75% chance to apply a nasty poison
        if (dice.roll(4) <= 3) {
            player.addEffect(new PoisonEffect(4, 4)); // 4 damage for 4 turns
            result += "\nYou have been poisoned by its venom!";
        }
        return result;
    }

    @Override
    public String getHint() {
        return "Hint: Its dripping fangs suggest a venomous threat.";
    }
}