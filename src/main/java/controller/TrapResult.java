package controller;

import model.Trap;

public record TrapResult(Trap trap, String imagePath, String messages, boolean playerDied) {
    
    public static TrapResult triggered(Trap trap, String messages, boolean playerDied) {
        return new TrapResult(trap, "images/ui/Trap.png", messages, playerDied);
    }

    public static TrapResult playerDiedEarly(String messages) {
        return new TrapResult(null, "images/ui/Trap.png", messages, true);
    }
}


