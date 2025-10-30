package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import utils.DiceRoller;

public class TrapFactory {
    private final DiceRoller dice;
    private final List<Supplier<Trap>> trapSuppliers = new ArrayList<>();

    public TrapFactory(DiceRoller dice) {
        this.dice = dice;
        trapSuppliers.add(SpikeTrap::new);
        trapSuppliers.add(PoisonDartTrap::new);
    }

    public Trap createRandomTrap() {
        int index = dice.rollIndex(trapSuppliers.size());
        return trapSuppliers.get(index).get();
    }
}