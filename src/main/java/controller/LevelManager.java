package controller;

//organizes, tracks, and provides access to the game’s levels and their enemies, and handles moving from one level to the next.
import java.util.List;
import java.util.function.Supplier;

import model.Enemy;
import model.Level;
import model.LevelFactory;
import utils.DiceRoller;

public class LevelManager {
    private final List<Level<Enemy>> gameLevels;
    private int currentLevelIndex;
    private final DiceRoller dice;

    public LevelManager(DiceRoller dice) {
        this.dice = dice;
        this.currentLevelIndex = 0;
        LevelFactory levelFactory = new LevelFactory();
        this.gameLevels = levelFactory.createAllLevels();
    }

    public Level<Enemy> getCurrentLevel() {
        return gameLevels.get(currentLevelIndex);
    }

    //randomizing the order of the enemy supplier for each level 
    public List<Supplier<Enemy>> getRandomizedEnemySuppliers() {
        return getCurrentLevel().getShuffledDeck(dice);
    }

    public boolean advanceToNextLevel() {
        if (currentLevelIndex < gameLevels.size() - 1) {
            currentLevelIndex++;
            return true;
        }
        return false;
    }
}