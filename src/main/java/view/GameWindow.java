package view;

import javax.swing.*;
import java.awt.*;

import com.dungeonescape.Game;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Dungeon Escape");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create panels
        DungeonPanel dungeonPanel = new DungeonPanel();
        InventoryPanel inventoryPanel = new InventoryPanel();
        LogPanel logPanel = new LogPanel();
        ControlPanel controlPanel = new ControlPanel();

        // Create the game controller and link it to the panels
        new Game(dungeonPanel, inventoryPanel, logPanel, controlPanel);

        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Add panels to the frame
        add(controlPanel, BorderLayout.NORTH);
        add(dungeonPanel, BorderLayout.CENTER);
        add(inventoryPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
    }
}