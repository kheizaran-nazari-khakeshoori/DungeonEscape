package system;

import java.util.Random;

import model.Enemy;
import model.Player;

public class CombatSystem {

    private Random dice = new Random();

    public void fight(Player player, Enemy enemy) {
        System.out.println("\nCombat begins: " + player.getName() + " vs " + enemy.getName() + "\n");

        // Fight loop until one dies
        while (player.isAlive() && enemy.isAlive()) {
            // Player attacks first
            if (dice.nextBoolean()) { // random chance to hit
                player.attack(enemy);
            } else {
                System.out.println(player.getName() + " missed the attack!");
            }

            if (!enemy.isAlive()) {
                System.out.println(enemy.getName() + " is defeated!");
                break;
            }

            // Enemy attacks
            if (dice.nextBoolean()) { // random chance to hit
                enemy.attack(player);
            } else {
                System.out.println(enemy.getName() + " missed the attack!");
            }

            if (!player.isAlive()) {
                System.out.println(player.getName() + " has been defeated!");
                break;
            }
        }
    }
}

