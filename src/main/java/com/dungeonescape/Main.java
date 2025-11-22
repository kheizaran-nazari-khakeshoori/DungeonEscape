package com.dungeonescape;

import javax.swing.SwingUtilities;

import view.PlayerSelectionWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PlayerSelectionWindow().setVisible(true);
        });
    }    
}
// //All Swing GUI code should run on the EDT(event dispatch thread) to avoid bugs and make sure the interface works smoothly.
// //invokeLater schedules your code (here, showing the PlayerSelectionWindow) to run safely on that thread.
// //It makes sure the game window is created and shown in a safe, thread-correct way for Java GUIs.