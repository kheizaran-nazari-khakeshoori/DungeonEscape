package model;

import exceptions.LevelLoadException;

public class Level {
    private String name;

    public Level(String name) {
        this.name = name;
    }

    public void load() throws LevelLoadException {
        System.out.println("Attempting to load level: " + name);
        // Simulate a loading error for demonstration purposes
        if (name.equalsIgnoreCase("Broken Level")) {
            throw new LevelLoadException("Failed to load 'Broken Level': Corrupted data or missing assets.");
        }
        System.out.println("Level '" + name + "' loaded successfully.");
    }
}
