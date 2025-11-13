package model;

public class MimicChest extends Enemy {
   public MimicChest() {
        
        super("Mimic Chest", 70, 18, 80, "images/enemies/MimicChest.png", DamageType.PHYSICAL);

        addResistance(DamageType.PIERCING, 0.8);
    }

    @Override
    protected String getAttackMessage() {
        return getName() + " reveals its teeth and chomps down on you for " +getBaseDamage()+ " damage!";
    }

    @Override
    public String getHint() {
        return "Hint: It looks like a treasure chest... but something feels off.";
    }
}