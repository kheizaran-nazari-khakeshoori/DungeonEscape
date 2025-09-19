package model;

public interface Effect {
    /**
     * Applies the effect's logic for a single turn or instance.
     * @param player The player to apply the effect to.
     */
    void apply(Player player);

    /**
     * Checks if the effect has completed its duration or purpose.
     * @return true if the effect is finished, false otherwise.
     */
    boolean isFinished();
}