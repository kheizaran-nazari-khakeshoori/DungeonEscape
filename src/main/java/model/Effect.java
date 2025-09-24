package model;

public interface Effect {
    /**
     * Applies the effect's logic for a single turn or instance.
     * @return A string describing what happened, for logging purposes.
     * @param player The player to apply the effect to.
     */
    String apply(Player player);

    /**
     * Checks if the effect has completed its duration or purpose.
     * @return true if the effect is finished, false otherwise.
     */
    boolean isFinished();

    /**
     * @return The name of the effect (e.g., "Poison", "Regeneration").
     */
    String getName();
}