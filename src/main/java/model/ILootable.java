package model;

import utils.DiceRoller;

public interface ILootable {
    Item dropLoot(DiceRoller dice);
    int getGoldValue();
}