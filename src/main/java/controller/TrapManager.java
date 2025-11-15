package controller;



import model.Player;
import model.Trap;
import model.TrapFactory;
import utils.DiceRoller;

public class TrapManager {
    private static final int TRAP_STRESS_DAMAGE = 5;
    private final TrapFactory trapFactory;
    private final DiceRoller dice;

    public TrapManager(TrapFactory trapFactory, DiceRoller dice) {
        this.trapFactory = trapFactory;
        this.dice = dice;
    }

    public TrapResult handleTrap(Player player,boolean attemptDisarm) {
        StringBuilder messages = new StringBuilder();
    
        player.takeDamage(TRAP_STRESS_DAMAGE);
        messages.append("The stress of finding a trap takes a toll on you...");
        messages.append("You lose ").append(TRAP_STRESS_DAMAGE).append(" HP\n");
        if (!player.isAlive()) {
           return TrapResult.playerDiedEarly(messages.toString());
        }
        
        Trap trap = trapFactory.createRandomTrap();
        messages.append(trap.getTriggerMessage()).append("\n");
        

        if (attemptDisarm && dice.getRandom().nextDouble() < player.getTrapDisarmChance()) {
           
            messages.append("Success! You deftly avoid the ").append(trap.getName()).append(".");
            return TrapResult.disarmed(trap, messages.toString());
        } 
        else {
            
            if (attemptDisarm) {
                messages.append("You failed to disarm the trap!\n");
            }

            String triggerResult = trap.trigger(player);
            messages.append(triggerResult);
            boolean playerDied = !player.isAlive();
            return TrapResult.triggered(trap, messages.toString(), playerDied);
        }
    
    }

    public int getDisarmChancePercent(Player player) {
        return (int) (player.getTrapDisarmChance() * 100);
    }
}