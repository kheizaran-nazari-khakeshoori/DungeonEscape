package com.dungeonescape;

import model.*;
import system.CombatSystem;
import exceptions.InvalidMoveException;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Hero");

        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);

        player.pickItem(sword);
        player.pickItem(potion);

        player.getInventory().showInventory();

        // Combat test
        Enemy goblin = new Goblin();
        CombatSystem combat = new CombatSystem();
        combat.fight(player, goblin);

        // Test invalid move
        try {
            player.useItem("Magic Wand"); // not in inventory
        } catch (InvalidMoveException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
