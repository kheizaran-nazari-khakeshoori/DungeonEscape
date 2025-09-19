package model;

public class ActiveEffect {
    private String name;
    private int healingPerTurn;
    private int remainingDuration;

    public ActiveEffect(String name, int healingPerTurn, int duration) {
        this.name = name;
        this.healingPerTurn = healingPerTurn;
        this.remainingDuration = duration;
    }

    // Applies the effect for one turn
    public void tick(Player player) {
        if (isFinished()) {
            return;
        }
        player.heal(healingPerTurn);
        remainingDuration--;
        if (isFinished()) {
            System.out.println("The effect of '" + name + "' has worn off.");
        }
    }

    public boolean isFinished() {
        return remainingDuration <= 0;
    }
}
