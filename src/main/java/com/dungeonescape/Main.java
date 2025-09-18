package com.dungeonescape;

import model.Player;
import model.Weapon;
import model.Potion;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Hero");

        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);

        player.pickItem(sword);
        player.pickItem(potion);

        player.getInventory().showInventory();

        // Test polymorphic use
        player.getInventory().useAll(player);
    }
}
