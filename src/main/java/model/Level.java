package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;

public class Level<T> {
    private final String name;
    private final List<Supplier<T>> encounterDeck;
    private final String backgroundImagePath;

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

    public List<Supplier<T>> getShuffledDeck(DiceRoller dice) {
        List<Supplier<T>> shuffled = new ArrayList<>(encounterDeck);
        Collections.shuffle(shuffled, dice.getRandom());
        return shuffled;
    }
}