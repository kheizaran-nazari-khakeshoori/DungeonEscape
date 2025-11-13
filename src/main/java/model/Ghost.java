package model;

public class Ghost extends Enemy {
    public Ghost() {
        super("Ghost", 40, 12, 25, "images/enemies/Ghost.png", DamageType.MAGIC);

        
        addResistance(DamageType.SLASHING, 0.5);
        addResistance(DamageType.PIERCING, 0.5);
        addWeakness(DamageType.HOLY, 2.0);
    }

    @Override
    protected String getAttackMessage() {
        return this.name + "can ruin you for" + getBaseDamage() + " damage!";
    }


    @Override
    public String getHint() {
        return "Hint: Physical attacks might be less effective.";
    }
}


