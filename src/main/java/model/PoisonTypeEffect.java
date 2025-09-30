package model;

/**
 * A marker interface to identify effects that are considered poisons.
 * This allows for polymorphic handling of different poison types (e.g., by an Antidote).
 */
public interface PoisonTypeEffect extends Effect<Player> {}