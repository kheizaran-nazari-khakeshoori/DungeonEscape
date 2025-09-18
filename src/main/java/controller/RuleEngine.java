package controller;

import model.Player;

public class RuleEngine {

    // Apply rules to a player
    public void applyRules(Player player) {
        // Rule: Player health cannot exceed 100
        if (player.getHealth() > 100) {
            player.setHealth(100);
            System.out.println("Rule applied: Player health capped at 100.");
        }
    }
}
