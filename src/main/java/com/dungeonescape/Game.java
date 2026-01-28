package com.dungeonescape;

// The Game class is the main controller for the entire game.
// It coordinates all major components and managers, connecting the game logic (combat, traps, levels, inventory, shop, etc.)
// with the user interface panels and handling the overall game flow.
// This class acts as the "brain" of the game, responding to user actions, updating the UI, and managing the state of the player, party, and encounters.
// It brings together different packages (model, controller, view) to ensure the game runs smoothly from start to finish.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.CombatManager;
import controller.DoorManager;
import controller.EncounterResult;
import controller.EncounterType;
import controller.FleeResult;
import controller.ItemUsageManager;
import controller.ItemUseResult;
import controller.LevelManager;
import controller.TrapManager;
import controller.TrapResult;
import exceptions.InvalidMoveException;
import model.Bean;
import model.Elfo;
import model.Enemy;
import model.Item;
import model.ItemFactory;
import model.Level;
import model.Lucy;
import model.Player;
import model.ShopEncounter;
import model.TrapFactory;
import model.Weapon;
import utils.DiceRoller;
import view.ControlPanel;
import view.DungeonPanel;
import view.GameWindow;
import view.InventoryDialog;
import view.InventoryPanel;
import view.LogPanel;
import view.PlayerListPanel;
import view.ShopDialog;
import view.StatusPanel;
import view.UIStateManager;

//class is responsible for overall game flow and coordination (managing encounters, UI updates, game state)

public class Game {
    
    private final Player activePlayer;
    private final List<Player> party;
    private Enemy currentEnemy;

    // Managers 
    private final DiceRoller dice;
    private final DoorManager doorManager;
    private final TrapManager trapManager;
    private final LevelManager levelManager;
    private final ItemUsageManager itemUsageManager;
    private final UIStateManager uiManager;
    private CombatManager combatManager;
    private final ShopEncounter shopEncounter;


    private final Map<String, Integer> enemyEncounterCount;

    public Game(Player player, GameWindow gameWindow, PlayerListPanel PlayerListPanel, DungeonPanel dungeonPanel, InventoryPanel inventoryPanel, LogPanel logPanel, ControlPanel controlPanel, StatusPanel hudPanel, ItemUsageManager itemUsageManager) {
        
        this.activePlayer = player;
        this.party = new ArrayList<>();
        initializeParty();
        this.dice = new DiceRoller();
        this.enemyEncounterCount = new HashMap<>();  
        TrapFactory trapFactory = new TrapFactory(dice);
        this.doorManager = new DoorManager(dice, enemyEncounterCount);
        this.trapManager = new TrapManager(trapFactory);
        this.levelManager = new LevelManager(dice);
        this.itemUsageManager = itemUsageManager;
        this.uiManager = new UIStateManager(dungeonPanel, controlPanel, inventoryPanel,
                                            hudPanel, PlayerListPanel, logPanel);
        this.shopEncounter = new ShopEncounter(ItemFactory.createShopItems());
        // Setup UI listeners
        setupListeners(dungeonPanel, controlPanel);
        // Start the game
        startGame();
    }

    // Demonstrates subtyping: List<Player> contains Elfo, Bean, Lucy (all subclasses of Player)
    private void initializeParty() {
        this.party.add(this.activePlayer);
        List<Player> allCharacters = new ArrayList<>();
        allCharacters.add(new Elfo());
        allCharacters.add(new Bean());
        allCharacters.add(new Lucy());

        for (Player character : allCharacters)
        {
            if (!character.getClass().equals(activePlayer.getClass())) 
            {
                party.add(character);
            }
        }
        //demonstrateClientSideMultityping();
    }

    // private void demonstrateClientSideMultityping() {
      
    //     Player p = new Elfo();        

        
    //     IEffectable e = p;            

    //     // Optional: root Object type (not required for gameplay)
    //     Object o = p;

    //     // Use the references to call methods
    //     e.addEffect(null);             // call via interface
    //     String name = p.getName();     // call via Player
    //     o.toString();                  // call via Object, just to demonstrate

    //     // Optional log to show the demo ran
    //     System.out.println("Client-side multi-typing demo: " + name);
    // }


    // connects the game's user interface buttons to the game logic using action listeners.
    private void setupListeners(DungeonPanel dungeonPanel, ControlPanel controlPanel) {
        dungeonPanel.door1Button.addActionListener(e -> chooseDoor(1));
        dungeonPanel.door2Button.addActionListener(e -> chooseDoor(2));
        controlPanel.addAttackListener(e -> performCombatRound());
        controlPanel.addSpecialListener(e -> useSpecialAbility());
        controlPanel.addFleeListener(e -> fleeEncounter());
        controlPanel.addInventoryListener(e -> manageInventory());
        controlPanel.addContinueListener(e -> {
            uiManager.setDoorMode();
            updateGUI();
        });
        controlPanel.addShopListener(e -> openShop());
        controlPanel.addExitListener(e -> System.exit(0));
    }

    private void startGame() {
        uiManager.getLogPanel().addMessage("Welcome to Dungeon Escape!");
        equipStartingWeapon();
        startNextLevel();
    }
    //automatically equips the first weapon found in the active plyer's inventory
    private void equipStartingWeapon() {
        Item startingWeapon = null;
        
        for(Item item :  activePlayer.getInventory().getItems())
        {
            if (item instanceof Weapon)
            {
                startingWeapon = item;//i am not calling specific weapon behavior here
                break;
            }
        }
        if (startingWeapon != null) {
            try {
                ItemUseResult result = itemUsageManager.useItem(activePlayer, startingWeapon.getName());
                uiManager.getLogPanel().addMessage(result.getMessage());
            } 
            catch (InvalidMoveException e) {
                uiManager.getLogPanel().addMessage("Error equipping starting weapon: " + e.getMessage());
            }
        }
    }

    private void startNextLevel() {
        Level<Enemy> level = levelManager.getCurrentLevel();
        doorManager.setEncounters(levelManager.getRandomizedEnemySuppliers());
        uiManager.loadBackgroundImage(level.getBackgroundImagePath());
        uiManager.getLogPanel().addMessage("\n--- You have entered " + level.getName() + " ---");
        uiManager.setDoorMode();
        updateGUI();//updating all panels 
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public LogPanel getLogPanel() {
        return uiManager.getLogPanel();
    }

    //They are private because only the Game class should manage the process of choosing doors and handling encounters, not outside code.
    private void chooseDoor(int doorNumber) {
        chooseDoor(doorNumber, null);
    }

    private void chooseDoor(int doorNumber, String enemyToAvoid) {
        if (currentEnemy != null) return; // Can't open a door during combat

        String doorName = (doorNumber == 1) ? "left" : "right";
        uiManager.getLogPanel().addMessage("\nYou open the " + doorName + " door...");
        // Delegate encounter generation to DoorManager
        EncounterResult encounter = doorManager.generateEncounter(activePlayer, enemyToAvoid);


        Map<EncounterType, Runnable> encounterHandlers = new HashMap<>();
        encounterHandlers.put(EncounterType.ENEMY, () -> handleEnemyEncounter(encounter.getEnemy()));
        encounterHandlers.put(EncounterType.TRAP, this::handleTrapEncounter);//refrenct to the current game object handle trap methode 
        encounterHandlers.put(EncounterType.EMPTY_ROOM, this::handleEmptyRoom);
        encounterHandlers.put(EncounterType.LEVEL_COMPLETE, this::handleLevelComplete);

        Runnable handler = encounterHandlers.get(encounter.getType());
        if (handler != null) handler.run();
    }
    //Whenever the player opens a door and finds an enemy, this method is called to begin the battle with that enemy.
    private void handleEnemyEncounter(Enemy enemy) {
        enterCombat(enemy);
    }
    

    private void handleTrapEncounter() {
        // Delegate to TrapManager
        TrapResult result = trapManager.handleTrap(activePlayer);
        uiManager.getLogPanel().addMessage("\n=== TRAP ENCOUNTERED ===");
        uiManager.displayImage(result.getImagePath());
        uiManager.getLogPanel().addMessage(result.getMessages());
        uiManager.getLogPanel().addMessage("========================\n");
        updateGUI();

        if (result.isPlayerDied()) {
            endGame();
        } else {
            uiManager.setPostEncounterMode();
        }
    }

    private void handleEmptyRoom() {
        uiManager.displayImage("images/ui/EmptyRoom.png");
        uiManager.getLogPanel().addMessage("The room is empty... but you spot something glinting in the corner!");
        activePlayer.addGold(10);
        uiManager.getLogPanel().addMessage("You found 10 gold!");
        updateGUI();
        uiManager.setPostEncounterMode();
    }

    private void handleLevelComplete() {
        uiManager.getLogPanel().addMessage("You've cleared this section of the dungeon!");
        if (levelManager.advanceToNextLevel()) {
            startNextLevel();
        } else {
            winGame();
        }
    }

    private void openShop() {
        if (currentEnemy != null) return; // Can't open shop during combat
        uiManager.getLogPanel().addMessage(shopEncounter.getEnterMessage());

        // Open the shop UI
        ShopDialog shopDialog = new ShopDialog(this, shopEncounter);
        shopDialog.setVisible(true);
        // The game is modal, so it will pause here until the dialog is closed.
        uiManager.getLogPanel().addMessage("You leave the shop.");
        updateGUI(); // Refresh GUI in case gold or items changed
    }

    private void enterCombat(Enemy enemy) {
        this.currentEnemy = enemy;
        String enemyName = enemy.getName();
        int previousEncounters = enemyEncounterCount.getOrDefault(enemyName, 0);
        this.combatManager = new CombatManager(activePlayer, currentEnemy, dice, previousEncounters); // Create combat manager with encounter count
        int level = previousEncounters + 1;
        uiManager.getLogPanel().addMessage("\nA " + enemyName + " (Lvl " + level + ") appears!");
        uiManager.getLogPanel().addMessage(enemy.getHint());
        uiManager.setCombatMode(activePlayer);
        uiManager.displayImage(enemy.getImagePath());
        updateGUI();
    }

    private void performCombatRound() {
        if (combatManager == null || !activePlayer.isAlive()) {
            return;
        }
        // Delegate all combat logic to the CombatManager
        String combatLog = combatManager.performCombatRound();
        uiManager.getLogPanel().addMessage(combatLog);

        // After the round, check the results
        if (!activePlayer.isAlive()) {
            endGame();
        } 
        else if (!currentEnemy.isAlive())
        {
            winCombat();
        }
        else 
        {
            uiManager.updateSpecialAbilityButton(activePlayer);
            updateGUI();
        }
    }

    private void useSpecialAbility() {
        if (combatManager == null || !activePlayer.isAlive()) {
            return;
        }

        // Delegate to the CombatManager
        String abilityResult = combatManager.useSpecialAbility();
        uiManager.getLogPanel().addMessage(abilityResult);

        // Check results after the ability and potential enemy counter-attack
        if (!activePlayer.isAlive()) {
            endGame();
        
        } else if (!currentEnemy.isAlive()) {
            winCombat();
    
        } else {
            uiManager.updateSpecialAbilityButton(activePlayer);
            updateGUI();
        }
    }

    private void winCombat() {
        String enemyName = currentEnemy.getName();
        uiManager.getLogPanel().addMessage("You defeated the " + enemyName + "!");

        // Update encounter count
        doorManager.Enemy_Appearance_count(enemyName);

        // Award loot
        Item loot = currentEnemy.dropLoot(dice);
        if (loot != null) {
            activePlayer.pickItem(loot);
            uiManager.getLogPanel().addMessage("The enemy dropped a " + loot.getName() + "!");
        }

        // Give the player some gold for winning the fight
        activePlayer.addGold(currentEnemy.getGoldValue());
        uiManager.getLogPanel().addMessage("You found " + currentEnemy.getGoldValue() + " gold!");

        // Check if the player died from a special ability (like Lucy's)
        if (!activePlayer.isAlive()) {
            endGame();
        } else {
            this.currentEnemy = null;
            this.combatManager = null;
            uiManager.setDoorMode();
            updateGUI();
        }
    }

    private void fleeEncounter() {
        if (combatManager == null) return;

        // Delegate flee logic to the CombatManager
        FleeResult result = combatManager.attemptFlee();
        uiManager.getLogPanel().addMessage(result.getLogMessage());

        if (result.isSuccess()) {
            String fledEnemyName = currentEnemy.getName();
            this.currentEnemy = null;
            this.combatManager = null;
            uiManager.setDoorMode(false); // Go to door mode, but don't clear the enemy image yet
            // Simulate choosing another door, ensuring a different enemy appears.
            int nextDoor = dice.roll(2);
            chooseDoor(nextDoor, fledEnemyName);
        } else {
            // Flee failed, check if player survived the enemy's attack
            if (!activePlayer.isAlive()) {
                endGame();
            }
            updateGUI();
        }
    }

    private void manageInventory() {
        // 1. Get data from the model
        List<String> itemNamesList = activePlayer.getInventory().getItemNames();
        // 2. Delegate UI interaction to a dedicated View component
        InventoryDialog inventoryDialog = new InventoryDialog();
        String selectedItem = inventoryDialog.showAndGetSelectedItem(itemNamesList);

        // If the user cancelled
        // 3. Process the result from the view
        if (selectedItem == null || selectedItem.isEmpty()) {
            return;
        }

        // Try to use the item by calling the dedicated manager
        try {
            ItemUseResult result = itemUsageManager.useItem(activePlayer, selectedItem);
            uiManager.getLogPanel().addMessage(result.getMessage());
            if (result.isSuccess()) {
                updateGUI(); // Update UI only on successful use
            }
        } catch (InvalidMoveException e) {
            uiManager.getLogPanel().addMessage("Could not use item: " + e.getMessage());
        }
    }


    public void updateGUI() {
        uiManager.updateAllPanels(activePlayer, party, currentEnemy);
    }

    private void endGame() {
        uiManager.getLogPanel().addMessage("\nYou have been defeated! GAME OVER.");
        uiManager.displayImage("images/ui/GameOver.png");
        uiManager.setGameOverMode();
    }

    private void winGame() {
        uiManager.getLogPanel().addMessage("\nCONGRATULATIONS! You have escaped the dungeon!");
        uiManager.displayImage("images/ui/Victory.png");
        uiManager.setGameOverMode();
    }
}