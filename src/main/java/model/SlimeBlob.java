package model;

public class SlimeBlob extends Enemy {
    public SlimeBlob() {
        
        super("Slime Blob", 60, 9, 20, "images/enemies/SlimeBlob.png", model.DamageType.ACID);

    
        resistances.put(DamageType.SLASHING, 0.5);
        resistances.put(DamageType.PIERCING, 0.5);
        resistances.put(DamageType.FIRE, 1.5);
    }

    @Override
    protected String getAttackMessage() {
        return this.name + " gooy cuases damage " + getBaseDamage() + " damage!";
    }



    @Override
    public String getHint() {
        return "Hint: Its gelatinous body seems to absorb physical blows. Maybe something else would work better?";
    }
}