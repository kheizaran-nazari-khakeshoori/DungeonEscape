package com.dungeonescape;

import model.Elfo;
import javax.swing.SwingUtilities;
import view.GameWindow;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting game with default hero...");
                new GameWindow(new Elfo()).setVisible(true);
            }
        });
    }
}
