package model;

public class Potion extends Item {
    private final int healAmount;

    public Potion(String name, String description, int healAmount, int quantity, String imagePath, int cost) {
        super(name, description, imagePath, cost);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void use(Player player) {
        player.heal(this.healAmount);
    }

    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public String getUseMessage(Player user) {
        return user.getName() + "used" + getName() + ".";//getname is inherited no super needed to use 
    }//you use super only when you are overriding and wanting parent's version  here i am not overrifing the getname i am inheriting from parent 

    @Override
    public String getStatsString() {
        return "<b>Heals: " + getHealAmount() + " HP</b>";//potion is calling its own method no need to super 
    }
}