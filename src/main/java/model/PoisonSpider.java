package model;
import utils.DiceRoller;

public class PoisonSpider extends Enemy {
     public PoisonSpider() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Poison Spider", 35, 6, 12, "images/enemies/PoisonSpider.png", model.DamageType.POISON);
    }


    @Override
    public String attack(ICombatant target, DiceRoller dice) throws exceptions.InvalidMoveException {
       Player player = (Player) target;
       target.takeDamage(this.baseDamage);
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