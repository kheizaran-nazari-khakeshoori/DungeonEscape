package controller;

import model.Player;
import model.Trap;
import model.TrapFactory;

// Used to manage traps
public class TrapManager {
   
    private final TrapFactory trapFactory;

    public TrapManager(TrapFactory trapFactory) {
        this.trapFactory = trapFactory;
     
    }

    public TrapResult handleTrap(Player player) {
        StringBuilder messages = new StringBuilder();

        // Create a new trap using the factory and dice
        Trap trap = trapFactory.createRandomTrap();

        // The trap is always triggered
        String triggerResult = trap.trigger(player);
        messages.append(triggerResult);
        boolean playerDied = !player.isAlive();

        if (playerDied) {
            return TrapResult.playerDiedEarly(messages.toString());
        } else {
            return TrapResult.triggered(trap, messages.toString(), false);
        }
    }

}