package model;
//organizes the data and encounters for one stage of the game
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

    //this methode shuffle and return a list of encounter supplier 
    public List<Supplier<T>> getShuffledDeck(DiceRoller dice) {
        List<Supplier<T>> shuffled = new ArrayList<>(encounterDeck);//creating copy of original encounter 
        Collections.shuffle(shuffled, dice.getRandom());
        return shuffled;
    }
}