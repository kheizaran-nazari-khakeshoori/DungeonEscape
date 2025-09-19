package com.dungeonescape;

import controller.CombatSystem;
import exceptions.InvalidMoveException;
import model.Enemy;
import model.Ghost;
import model.Goblin;
import model.Player;
import model.Potion;
import model.Weapon;

public class Main {
    public static void main(String[] args) {
        // Create player and inventory
        Player player = new Player("Hero");
        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);

        player.pickItem(sword);
        player.pickItem(potion);

        player.showInventory();

        // Create RuleEngine and CombatSystem
        CombatSystem combat = new controller.CombatSystem();

        // Combat against enemies
        Enemy goblin = new Goblin();
        Enemy ghost = new Ghost();

        // Player starts at 100 health
        combat.fight(player, goblin);

        // If player is still alive, they can use items
        if (player.isAlive()) {
            System.out.println("\n" + player.getName() + " survived the fight! Current health: " + player.getHealth());
            System.out.println("Using a health potion to recover...");
            try {
                player.useItem("Health Potion"); // The heal method now caps health at 100
            } catch (InvalidMoveException e) {
                System.out.println("Exception caught: " + e.getMessage());
            }
        }

        // Test invalid item usage
        try {
            player.useItem("Magic Wand"); // not in inventory
        } catch (InvalidMoveException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
