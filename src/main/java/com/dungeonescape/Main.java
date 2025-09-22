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

        // Player must equip a weapon to use it in combat
        try {
            System.out.println("\n" + player.getName() + " is equipping their weapon...");
            player.useItem("Sword");
        } catch (InvalidMoveException e) {
            System.out.println("Could not equip weapon: " + e.getMessage());
        }

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
            System.out.println("\n" + player.getName() + " survived the first fight! Current health: " + player.getHealth());
            System.out.println("Using a health potion to prepare for the next foe...");
            try {
                player.useItem("Health Potion"); // The heal method now caps health at 100
                player.useItem("Health Potion"); // This will start a healing-over-time effect
            } catch (InvalidMoveException e) {
                System.out.println("Exception caught: " + e.getMessage());
            }

            System.out.println("\nSuddenly, another enemy appears!");
            combat.fight(player, ghost); // Fight the ghost, healing effect will apply each turn
        }

        // Demonstrate Overloading after combat
        if (player.isAlive()) {
            System.out.println("\n--- Demonstrating Method Overloading ---");
            System.out.println("The hero practices on a training dummy.");
            Enemy dummy = new Goblin(); // A new goblin to practice on
            player.attack(dummy);      // Calls the standard attack(Enemy)
            player.attack(dummy, 15);  // Calls the new overloaded attack(Enemy, int)
        }

        // Test invalid item usage
        try {
            player.useItem("Magic Wand"); // not in inventory
        } catch (InvalidMoveException e) {
            System.out.println("Exception caught: " + e.getMessage());
            System.out.println("\nException caught: " + e.getMessage());
        }
    }
}
