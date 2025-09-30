package controller;

import exceptions.InvalidMoveException;
import model.Enemy;
import model.Player;
import utils.DiceRoller;

public class CombatSystem {
    private DiceRoller dice;

    public CombatSystem() {
        dice = new DiceRoller();
    }

    public void fight(Player player, Enemy enemy) {
        System.out.println("\nCombat begins: " + player.getName() + " vs " + enemy.getName() + "\n");

        while (player.isAlive() && enemy.isAlive()) {
            // Player turn
            player.applyTurnEffects();

            if (!player.isAlive()) {
                // This check is for future effects like poison
                break;
            }

            int playerRoll = dice.roll(6); // roll a 6-sided dice
            if (playerRoll > 1) { // hit if roll > 1
                try {
                    String attackResult = player.attack(enemy, dice);
                    System.out.println(attackResult);
                } catch (InvalidMoveException e) {
                    // Handle the case where the player cannot attack (e.g., no weapon)
                    System.out.println(player.getName() + " tries to attack but fails: " + e.getMessage());
                }
            } else {
                System.out.println(player.getName() + " missed the attack!");
            }

            if (!enemy.isAlive()) {
                System.out.println(enemy.getName() + " is defeated!");
                break;
            }

            // Enemy turn
            int enemyRoll = dice.roll(6);
            if (enemyRoll > 1) {
                enemy.attack(player, dice);
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
