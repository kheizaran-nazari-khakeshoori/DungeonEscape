package controller;

import model.Player;

public class RuleEngine {

    // Apply rules to a player
    public void applyRules(Player player) {
        // This specific rule is now handled by the Player.heal() method.
        // This engine could be used for more complex, external rules in the future.
        System.out.println("Applying game rules...");
    }
}
