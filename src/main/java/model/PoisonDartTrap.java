package model;

public class PoisonDartTrap extends Trap {

    public PoisonDartTrap() {
        super("Poison Dart Trap", "A small hiss echoes in the room as a dart flies from the wall!");
    }

    @Override
    public String trigger(Player player) {
        player.addEffect(new PoisonEffect(5, 3)); 
        return "The dart hits you! You feel a venomous sting. You have been poisoned!";
    }
}