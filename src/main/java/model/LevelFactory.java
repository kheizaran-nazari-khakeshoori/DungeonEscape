package model;

import java.util.ArrayList;
import java.util.List;

//Factory for creating game levels with enemy configurations.
public class LevelFactory {
    
  
    public List<Level<Enemy>> createAllLevels() {
        List<Level<Enemy>> levels = new ArrayList<>();
        
        levels.add(createGoblinWarrens());
        levels.add(createHauntedHalls());
        levels.add(createCreatureCaves());
        
        return levels;
    }
    
    private Level<Enemy> createGoblinWarrens() {
        return new Level<>(
            "The Goblin Warrens",
            "images/ui/TwoDoors.png",
            List.of(Goblin::new, Goblin::new, StoneMan::new , PoisonSpider::new)
        );
    }
    
    private Level<Enemy> createHauntedHalls() {
        return new Level<>(
            "The Haunted Halls",
            "images/ui/HauntedHalls.png",
            List.of(SkeletonWarrior::new, Ghost::new, Ghost::new, ShadowAssassin::new)
        );
    }
    
    private Level<Enemy> createCreatureCaves() {
        return new Level<>(
            "The Creature Caves",
            "images/ui/CreatureCaves.png",
            List.of(PoisonSpider::new, SlimeBlob::new, StoneMan::new, MimicChest::new)
        );
    }
    
    // Easy to add new levels in the future:
    // private Level<Enemy> createDragonLair() { ... }
}
