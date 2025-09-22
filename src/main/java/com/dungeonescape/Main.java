package com.dungeonescape;

import javax.swing.SwingUtilities;
import view.PlayerSelectionWindow;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Showing player selection screen...");
                new PlayerSelectionWindow().setVisible(true);
            }
        });
    }
}
