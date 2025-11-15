package com.dungeonescape;

import javax.swing.SwingUtilities;

import view.PlayerSelectionWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PlayerSelectionWindow().setVisible(true);
        });
        

    }
    public static String println(String name)
    {
        return name;
    }
}