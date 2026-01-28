package model;

public class Zock extends Player {

    public Zock()

    {
        super("Zock",85);
        
    }

    @Override
    public String useSpecialAbility(Iwarrior target) {
        int zockdamage = 30;
        int weakness = 15;

        String combatMessage = target.takeDamage( zockdamage,null);
        this.takeDamage(weakness);
        
        return combatMessage;
    }

    @Override
    public String getImagePath() {
        
       return "images/Players/Bean.png";
    }
    
}
