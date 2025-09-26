package model;

/**
 * A generic interface for status effects that can be applied to a target.
 * @param <T> The type of character this effect can be applied to (e.g., Player, Enemy).
 */
public interface Effect<T> {
    /**
     * Applies the effect's logic to the target for one turn.
     * @param target The character to apply the effect to.
     * @return A string describing what happened.
     */
    String apply(T target);

    /**
     * Checks if the effect has finished and should be removed.
     * @return true if the effect is finished, false otherwise.
     */
    boolean isFinished();

    /**
     * Gets the name of the effect (e.g., "Poison", "Regeneration").
     * @return The name of the effect (e.g., "Poison", "Regeneration").
     */
    String getName();
}