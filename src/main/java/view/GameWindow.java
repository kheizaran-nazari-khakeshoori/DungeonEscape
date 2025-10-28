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
        setLocationRelativeTo(null); // Center the window

        // Create panels
        PartyPanel partyPanel = new PartyPanel();
        DungeonPanel dungeonPanel = new DungeonPanel();
        InventoryPanel inventoryPanel = new InventoryPanel();
        LogPanel logPanel = new LogPanel();
        ControlPanel controlPanel = new ControlPanel();
        HUDPanel hudPanel = new HUDPanel();
        ItemUsageManager itemUsageManager = new ItemUsageManager();
        // Create the game controller and link it to the panels
        new Game(player, this, partyPanel, dungeonPanel, inventoryPanel, logPanel, controlPanel, hudPanel,itemUsageManager);

        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Add panels to the frame
        add(controlPanel, BorderLayout.NORTH);

        // Combine Party and HUD panels on the left
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(hudPanel, BorderLayout.NORTH);
        westPanel.add(partyPanel, BorderLayout.CENTER);
        add(westPanel, BorderLayout.WEST);

        add(dungeonPanel, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
    }
}