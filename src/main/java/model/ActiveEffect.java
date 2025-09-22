package model;

public class ActiveEffect implements Effect {
    private String name;
    private int healingPerTurn;
    private int remainingDuration;

    public ActiveEffect(String name, int healingPerTurn, int duration) {
        this.name = name;
        this.healingPerTurn = healingPerTurn;
        this.remainingDuration = duration;
    }

    // Applies the effect for one turn
    @Override
    public void apply(Player player) {
        if (isFinished()) {
            return;
        }
        player.heal(healingPerTurn);
        remainingDuration--;
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }
}
