package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;

/**
 * A generic class representing a level in the dungeon.
 * It uses Parametric Polymorphism (Generics) to hold a deck of any type of encounter,
 * such as Enemies or Traps.
 * @param <T> The type of encounter this level contains (e.g., Enemy).
 */
public class Level<T> {
    private String name;
    private List<Supplier<T>> encounterDeck;
    private String backgroundImagePath;

    public Level(String name, String backgroundImagePath, List<Supplier<T>> encounters) {
        this.name = name;
        this.backgroundImagePath = backgroundImagePath;
        this.encounterDeck = new ArrayList<>(encounters);
    }

    public String getName() {
        return name;
    }
    
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    /**
     * Shuffles the level's encounter deck and returns it.
     * @param dice The dice roller to use for shuffling.
     * @return A new shuffled list of encounter suppliers.
     */
    public List<Supplier<T>> getShuffledDeck(DiceRoller dice) {
        List<Supplier<T>> shuffled = new ArrayList<>(encounterDeck);
        Collections.shuffle(shuffled, dice.getRandom());
        return shuffled;
    }
}