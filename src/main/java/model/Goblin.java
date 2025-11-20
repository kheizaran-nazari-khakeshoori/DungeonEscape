package model;
import utils.DiceRoller;

public class Goblin extends Enemy {
     public Goblin() {
        
        super("Goblin", 30, 8, 10, "images/enemies/Goblin.png",DamageType.PHYSICAL);
       
    }


   @Override
    public String attack(Iwarrior target, DiceRoller dice) {
 
   
    target.takeDamage(getBaseDamage());
    String result = getName() + " attacks you for " + getBaseDamage() + " damage.";
    
    if (target instanceof Player player) // instanceof tells you what an object really is at runtime
    {
        //apply poison is boring so diece roller help me to make it as a special case 
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
    protected String getAttackMessage() {
        return getName() + " attacks you for " + getBaseDamage() + " damage.";
    }


    @Override
    public String getHint() {
        return "Hint: This creature looks straightforward. A standard weapon should suffice.";
    }
}