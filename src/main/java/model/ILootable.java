package model;

import utils.DiceRoller;

public interface ILootable {//the item or the thing we can get 
    Item dropLoot(DiceRoller dice);
    int getGoldValue();
}