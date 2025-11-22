package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dungeonescape.Game;

import controller.ItemUsageManager;
import model.Player;

public class GameWindow extends JFrame {

    public GameWindow(Player player) {
        setTitle("Dungeon Escape");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // Create panels
        PlayerListPanel PlayerListPanel = new PlayerListPanel();//players list 
        DungeonPanel dungeonPanel = new DungeonPanel();//images and doors 
        InventoryPanel inventoryPanel = new InventoryPanel();//inventory 
        LogPanel logPanel = new LogPanel();//log messages 
        ControlPanel controlPanel = new ControlPanel();//actions
        StatusPanel statusPanel = new StatusPanel(); //satus 

        ItemUsageManager itemUsageManager = new ItemUsageManager();
        
        //calling the game.java constructor  
        new Game(player, this, PlayerListPanel, dungeonPanel, inventoryPanel, logPanel, controlPanel,statusPanel,itemUsageManager);


        //deviding window into the five regions
        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(statusPanel, BorderLayout.NORTH);
        westPanel.add(PlayerListPanel, BorderLayout.CENTER);

        add(westPanel, BorderLayout.WEST);
        add(dungeonPanel, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
    }
}