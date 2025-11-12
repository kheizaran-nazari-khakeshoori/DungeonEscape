package controller;

import model.Trap;


//Result of a trap encounter.
//Contains trap information, messages, and outcome status.

public record TrapResult(
    Trap trap,
    String imagePath,
    String messages,
    boolean trapDisarmed,
    boolean playerDied
) 
{
    
    public static TrapResult disarmed(Trap trap, String messages) {
        return new TrapResult(
            trap,
            "images/ui/Trap.png",
            messages,
            true,  // trapDisarmed
            false  // playerDied
        );
    }
    
   
    public static TrapResult triggered(Trap trap, String messages, boolean playerDied) {
        return new TrapResult(
            trap,
            "images/ui/Trap.png",
            messages,
            false,  // trapDisarmed
            playerDied
        );
    }
    
  
    public static TrapResult playerDiedEarly(String messages) {
        return new TrapResult(
            null,  // no trap
            "images/ui/Trap.png",
            messages,
            false,  // trapDisarmed
            true    // playerDied
        );
    }
}
