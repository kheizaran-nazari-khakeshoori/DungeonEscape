package model;
//most specific so Antitide is an item that heals 
public class Antidote extends Potion {

    public Antidote(String name, String description, String imagePath, int cost) {
        super(name, description, 10, 1, imagePath, cost); 
    }

    @Override
    public void use(Player player) {
        super.use(player); 
        player.removeEffectsOfType(PoisonTypeEffect.class);//add extra behavior 
    }

    @Override
    public String getUseMessage(Player user) {
        return "You used the " + getName() + ". The poison subsides and you recover " + getHealAmount() + " health!";
    }

    @Override
    public String getStatsString() {
        return "<b>Cures Poison</b>";
    }
}