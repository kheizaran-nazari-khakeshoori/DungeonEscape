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
        PlayerListPanel PlayerListPanel = new PlayerListPanel();
        DungeonPanel dungeonPanel = new DungeonPanel();
        InventoryPanel inventoryPanel = new InventoryPanel();
        LogPanel logPanel = new LogPanel();
        ControlPanel controlPanel = new ControlPanel();
        HUDPanel hudPanel = new HUDPanel();

        ItemUsageManager itemUsageManager = new ItemUsageManager();
        
        //this >> is current window  
        new Game(player, this, PlayerListPanel, dungeonPanel, inventoryPanel, logPanel, controlPanel, hudPanel,itemUsageManager);


        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(hudPanel, BorderLayout.NORTH);
        westPanel.add(PlayerListPanel, BorderLayout.CENTER);

        add(westPanel, BorderLayout.WEST);
        add(dungeonPanel, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
    }
}