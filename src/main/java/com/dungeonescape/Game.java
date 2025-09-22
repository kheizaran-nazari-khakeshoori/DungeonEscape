package com.dungeonescape;

import java.util.Random;
import java.util.Scanner;

import controller.CombatSystem;
import exceptions.InvalidMoveException;
import model.Enemy;
import model.EnemyFactory;
import model.Player;
import model.Potion;
import model.Weapon;

/**
 * The main controller for the game, managing the game state and main loop.
 */
public class Game {
    private Player player;
    private EnemyFactory enemyFactory;
    private CombatSystem combatSystem;
    private Scanner scanner;
    private Random random;
    private boolean isRunning;

    public Game() {
        this.player = new Player("Hero");
        this.enemyFactory = new EnemyFactory();
        this.combatSystem = new CombatSystem();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.isRunning = true;
    }

    /**
     * Starts and runs the main game loop.
     */
    public void run() {
        setupGame();

        while (isRunning && player.isAlive()) {
            printMainMenu();
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "E":
                case "EXPLORE":
                    explore();
                    break;
                case "I":
                case "INVENTORY":
                    manageInventory();
                    break;
                case "Q":
                case "QUIT":
                    isRunning = false;
                    System.out.println("You decide to rest. Game over.");
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
                    break;
            }
        }

        if (!player.isAlive()) {
            System.out.println("\nGAME OVER. You have been defeated.");
        }

        scanner.close();
        System.out.println("Thanks for playing Dungeon Escape!");
    }

    /**
     * Initializes the player with starting items and sets up the game state.
     */
    private void setupGame() {
        System.out.println("Welcome to Dungeon Escape!");
        Weapon sword = new Weapon("Sword", 25, 3);
        Potion potion = new Potion("Health Potion", 50, 2);
        player.pickItem(sword);
        player.pickItem(potion);

        System.out.println("\nYou start your journey with a sword and a potion.");
        try {
            player.useItem("Sword"); // Auto-equip sword
        } catch (InvalidMoveException e) {
            System.out.println("Critical setup error: " + e.getMessage());
            isRunning = false;
        }
    }

    /**
     * Prints the player's status and the main menu options.
     */
    private void printMainMenu() {
        System.out.println("\n========================================");
        player.showStatus();
        System.out.println("What do you want to do?");
        System.out.println("  [E]xplore the dungeon");
        System.out.println("  [I]nventory management");
        System.out.println("  [Q]uit game");
        System.out.print("> ");
    }

    /**
     * Handles the logic for exploring, which can lead to finding an enemy, an item, or nothing.
     */
    private void explore() {
        System.out.println("\nYou venture deeper into the dungeon...");
        int eventChance = random.nextInt(10); // 0-9

        if (eventChance < 6) { // 60% chance to find an enemy
            Enemy enemy = enemyFactory.createRandomEnemy();
            System.out.println("A wild " + enemy.getName() + " appears!");
            combatSystem.fight(player, enemy);
        } else if (eventChance < 9) { // 30% chance to find an item (pickItem)
            System.out.println("You found a treasure chest!");
            Potion foundPotion = new Potion("Lesser Potion", 25, 1);
            System.out.println("You found a '" + foundPotion.getName() + "'. Do you want to pick it up? (Y/N)");
            System.out.print("> ");
            String choice = scanner.nextLine().trim().toUpperCase();
            if (choice.equals("Y")) {
                player.pickItem(foundPotion);
            } else {
                System.out.println("You leave the potion behind.");
            }
        } else { // 10% chance for nothing
            System.out.println("The corridor is empty. You continue on your way.");
        }
    }

    /**
     * Manages the inventory screen, allowing the player to use items.
     */
    private void manageInventory() {
        boolean inInventoryMenu = true;
        while (inInventoryMenu) {
            System.out.println("\n--- Inventory ---");
            player.showInventory();
            System.out.println("Enter item name to use, or [B]ack to return.");
            System.out.print("> ");
            String itemChoice = scanner.nextLine().trim();

            if (itemChoice.equalsIgnoreCase("B") || itemChoice.equalsIgnoreCase("BACK")) {
                inInventoryMenu = false;
            } else if (!itemChoice.isEmpty()) {
                try {
                    player.useItem(itemChoice);
                } catch (InvalidMoveException e) {
                    System.out.println("Could not use item: " + e.getMessage());
                }
            } else {
                System.out.println("No item specified.");
            }
        }
    }
}