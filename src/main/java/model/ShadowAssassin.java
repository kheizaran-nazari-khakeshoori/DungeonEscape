package model;

public class ShadowAssassin extends Enemy {
     public ShadowAssassin() {
        
        super("Shadow Assassin", 55, 20, 40, "images/enemies/ShadowAssassin.png", DamageType.SHADOW);
    }


    @Override
    protected String getAttackMessage() {
        return this.name + " shadow assasin causes damage for " + getBaseDamage()+ " damage!";
    }


    @Override
    public String getHint() {
        return "Hint: It moves with blinding speed. A quick, decisive blow might be your only chance.";
    }
}