package model;


public class Defensive_type_Effect implements Effect<Player>  {
    //static : Belongs to the CLASS, not to individual objects
    public static final String EFFECT_NAME = "Defensive Stance";//is a constant - a value that never changes and is shared by all instances of the class.
    private int remainingDuration;

    public Defensive_type_Effect(int duration) {
        this.remainingDuration = duration;
    }

    @Override
    public String apply(Player player) {
        if (!isFinished()) {
            remainingDuration--;
        }
        return player.getName() + " is in a defensive stance!";
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public String getName() {
        return EFFECT_NAME;
    }

    public int applyDefense(int incomingDamage) {
        return incomingDamage / 2; 
    }
}