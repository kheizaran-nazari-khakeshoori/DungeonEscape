package model;

public class SpikeTrap extends Trap {

    public SpikeTrap() {
        super("Spike Trap", "You hear a *click* underfoot... spikes shoot up from the floor!");
    }

    @Override
    public String trigger(Player player) {
        int damage = 20;
        player.takeDamage(damage);
        return "The spikes impale you for " + damage + " damage!";
    }
}