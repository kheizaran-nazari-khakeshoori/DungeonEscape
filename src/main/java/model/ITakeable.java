package model;

import utils.DiceRoller;

public interface ITakeable {
    Item dropLoot(DiceRoller dice);
    int getGoldValue();
}