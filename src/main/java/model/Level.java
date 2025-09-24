package model;

import exceptions.LevelLoadException;

public class Level {
    private String name;

    public Level(String name) {
        this.name = name;
    }

    public void load() throws LevelLoadException {
        // In a real game, this would load data from a file.
        // For now, it just simulates the process.
        // Simulate a loading error for demonstration purposes
        if (name.equalsIgnoreCase("Broken Level")) {
            throw new LevelLoadException("Failed to load 'Broken Level': Corrupted data or missing assets.");
        }
    }
}
