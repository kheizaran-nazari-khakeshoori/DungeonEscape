package model;

//used to reduce the damage income 
public class Defensive_type_Effect implements Effect<Player>  {
    
    public static final String EFFECT_NAME = "Defensive Stance";
    private int remainingDuration;

    public Defensive_type_Effect(int duration) {
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (!isFinished()) {
            remainingDuration--;
            if (remainingDuration == 0)
            {
                return player.getName() + "is in defensive mode!";
            }
        }
        return "";
       
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() {
        return EFFECT_NAME;
    }

    @Override
    public int modifyIncomingDamage(int incomingDamage) {
        return incomingDamage / 2; // Reduces damage by 50%
    }
}