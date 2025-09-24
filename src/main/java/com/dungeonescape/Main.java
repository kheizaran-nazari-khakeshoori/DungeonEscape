package com.dungeonescape;

import javax.swing.SwingUtilities;

import view.PlayerSelectionWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Launch the visual player selection window
            new PlayerSelectionWindow().setVisible(true);
        });
    }
}