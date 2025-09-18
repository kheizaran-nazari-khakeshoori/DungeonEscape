package com.dungeonescape;

import model.Enemy;
import model.Ghost;
import model.Goblin;
import model.Player;
import model.Potion;
import model.StoneMan;
import model.Weapon;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Hero");

        Weapon sword = new Weapon("Sword", 25);
        Potion potion = new Potion("Health Potion", 50);

        player.pickItem(sword);
        player.pickItem(potion);

        player.getInventory().showInventory();

        Enemy goblin = new Goblin();
        Enemy ghost = new Ghost();
        Enemy stoneMan = new StoneMan();

        goblin.attack(player);
        ghost.move();
        stoneMan.attack(player);

    }
    
}
