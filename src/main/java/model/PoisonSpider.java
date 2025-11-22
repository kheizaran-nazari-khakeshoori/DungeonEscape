package model;


public class PoisonSpider extends Enemy {
     public PoisonSpider() {
        // Name, Health, Base Damage, Gold Value, Image Path, Damage Type
        super("Poison Spider", 35, 6, 12, "images/enemies/PoisonSpider.png", model.DamageType.POISON);
    }

    @Override
    protected String getAttackMessage() {
        return getName() + " bites you for " + getBaseDamage()+ " damage.";
    }

    @Override
    public String getHint() {
        return "Hint: Its dripping fangs suggest a venomous threat.";
    }
}