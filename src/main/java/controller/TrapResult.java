package controller;

import model.Trap;

public class TrapResult {
    private final Trap trap;
    private final String imagePath;
    private final String messages;
    private final boolean playerDied;

    public TrapResult(Trap trap, String imagePath, String messages, boolean playerDied) {
        this.trap = trap;
        this.imagePath = imagePath;
        this.messages = messages;
        this.playerDied = playerDied;
    }

    public Trap getTrap() { return trap; }
    public String getImagePath() { return imagePath; }
    public String getMessages() { return messages; }
    public boolean isPlayerDied() { return playerDied; }

    public static TrapResult triggered(Trap trap, String messages, boolean playerDied) {
        return new TrapResult(trap, "images/ui/Trap.png", messages, playerDied);
    }

    public static TrapResult playerDiedEarly(String messages) {
        return new TrapResult(null, "images/ui/Trap.png", messages, true);
    }
}