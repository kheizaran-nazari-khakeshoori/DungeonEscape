package controller;

import model.Enemy;

public class EncounterResult {
    private final EncounterType type;
    private final Enemy enemy;

    public EncounterResult(EncounterType type, Enemy enemy) {
        this.type = type;
        this.enemy = enemy;
    }

    public EncounterType getType() {
        return type;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}