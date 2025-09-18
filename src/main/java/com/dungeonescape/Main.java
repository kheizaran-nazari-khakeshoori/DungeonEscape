package com.dungeonescape;

import model.*;
import system.CombatSystem;
import controller.RuleEngine;
import exceptions.InvalidMoveException;

public class Main {
    public static void main(String[] args) {
        // Create player and inventory
        Player player = new Player("Hero");
        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);

        player.pickItem(sword);
        player.pickItem(potion);

        player.getInventory().showInventory();

        // Create RuleEngine and CombatSystem
        RuleEngine ruleEngine = new RuleEngine();
        CombatSystem combat = new CombatSystem();

        // Combat against enemies
        Enemy goblin = new Goblin();
        Enemy ghost = new Ghost();

        combat.fight(player, goblin);
        combat.fight(player, ghost);

        // Player uses potion (may exceed max health)
        try {
            player.useItem("Health Potion");
        } catch (InvalidMoveException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        // Apply rules after using potion
        ruleEngine.applyRules(player);
        System.out.println("Player health after rules: " + player.getHealth());

        // Test invalid item usage
        try {
            player.useItem("Magic Wand"); // not in inventory
        } catch (InvalidMoveException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
