package com.dungeonescape;

import view.GameWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameWindow().setVisible(true);
            }
        });
    }
}
