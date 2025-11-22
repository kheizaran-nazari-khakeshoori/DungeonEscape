package model;


public class Goblin extends Enemy {
     public Goblin() {
        
        super("Goblin", 30, 8, 10, "images/enemies/Goblin.png",DamageType.PHYSICAL);
       
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