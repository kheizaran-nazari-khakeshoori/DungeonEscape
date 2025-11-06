package model;
import utils.DiceRoller;

public class Goblin extends Enemy {
     public Goblin() {
        
        super("Goblin", 30, 8, 10, "images/enemies/Goblin.png", model.DamageType.PHYSICAL);
       
    }


   @Override
    public String attack(Iwarrior target, DiceRoller dice) throws exceptions.InvalidMoveException {
 
    target.takeDamage(this.baseDamage);
    String result = this.name + " attacks you for " + this.baseDamage + " damage.";
    
    if (target instanceof Player player)
    {
        int poisonChance = dice.roll(4);
        if (poisonChance == 1) {  
        
        int poisonDamage = 2;
        int poisonDuration = 2;
        player.addEffect(new GoblinPoisonEffect(poisonDamage, poisonDuration));
        
        result += "\nYou are hit by a rusty weapon. Now you are poisoned!";
    }
    }
    
    return result;
}

    @Override
    public String getHint() {
        return "Hint: This creature looks straightforward. A standard weapon should suffice.";
    }
}