package controller;

import javax.swing.JOptionPane;

import model.Player;
import model.Trap;
import model.TrapFactory;
import utils.DiceRoller;

public class TrapManager {
    private final TrapFactory trapFactory;
    private final DiceRoller dice;

    public TrapManager(TrapFactory trapFactory, DiceRoller dice) {
        this.trapFactory = trapFactory;
        this.dice = dice;
    }

    public TrapResult handleTrap(Player player) {
        TrapResult result = new TrapResult();
    
        player.takeDamage(5);
        result.addMessage("The stress of finding a trap takes a toll on you...");
        result.addMessage("You lose 5 HP!");
        if (!player.isAlive()) {
            result.playerDied = true;
            return result;
        }
        
        Trap trap = trapFactory.createRandomTrap();
        result.trap = trap;
        result.addMessage(trap.getTriggerMessage());
        result.imagePath = "images/ui/Trap.png";
        
        int disarmChancePercent = (int) (player.getTrapDisarmChance() * 100);
        int choice = JOptionPane.showConfirmDialog(
            null,
            "A " + trap.getName() + "!\nAttempt to disarm/avoid it? (Chance: " + disarmChancePercent + "%)",
            "It's a Trap!",
            JOptionPane.YES_NO_OPTION
        );
        boolean attemptDisarm = (choice == JOptionPane.YES_OPTION);
        if (attemptDisarm && dice.getRandom().nextDouble() < player.getTrapDisarmChance()) {
           
            result.addMessage("Success! You deftly avoid the " + trap.getName() + ".");
            result.trapDisarmed = true;
        } else {
            
            if (attemptDisarm) {
                result.addMessage("You failed to disarm the trap!");
            }
            String triggerResult = trap.trigger(player);
            result.addMessage(triggerResult);
            if (!player.isAlive()) {
                result.playerDied = true;
            }
        }
        return result;
    }

    public static class TrapResult { 
        public StringBuilder messages = new StringBuilder(); 
        public Trap trap; 
        public String imagePath; 
        public boolean trapDisarmed = false; 
        public boolean playerDied = false;

        public void addMessage(String msg) {
            messages.append(msg).append("\n");
        }

        public String getAllMessages() { return messages.toString().trim(); } 
    }
}