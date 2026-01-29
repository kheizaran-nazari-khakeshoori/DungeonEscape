package controller;

import model.Enemy;

public record EncounterResult(EncounterType type, Enemy enemy) {
}