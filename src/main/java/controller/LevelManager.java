package controller;

//organizes, tracks, and provides access to the game’s levels and their enemies, and handles moving from one level to the next.
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import model.Enemy;
import model.Ghost;
import model.Goblin;
import model.Level;
import model.MimicChest;
import model.PoisonSpider;
import model.ShadowAssassin;
import model.SkeletonWarrior;
import model.SlimeBlob;
import model.StoneMan;
import utils.DiceRoller;

public class LevelManager {
    private final List<Level<Enemy>> gameLevels;
    private int currentLevelIndex;
    private final DiceRoller dice;

    public LevelManager(DiceRoller dice) {
        this.dice = dice;
        this.gameLevels = new ArrayList<>(); 
        this.currentLevelIndex = 0;
        initializeLevels();
    }

    private void initializeLevels() {
        //This is called method reference syntax (specifically constructor reference).
        gameLevels.add(new Level<>("The Goblin Warrens", "images/ui/TwoDoors.png",
            List.of(Goblin::new, Goblin::new, SkeletonWarrior::new))); //Goblin::new is a reference to the Goblin constructor  like writing ->  new Goblin()
       
        gameLevels.add(new Level<>("The Haunted Halls", "images/ui/HauntedHalls.png",
            List.of(SkeletonWarrior::new, Ghost::new, Ghost::new, ShadowAssassin::new)));
       
        gameLevels.add(new Level<>("The Creature Caves", "images/ui/CreatureCaves.png",
            List.of(PoisonSpider::new, SlimeBlob::new, StoneMan::new, MimicChest::new)));
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