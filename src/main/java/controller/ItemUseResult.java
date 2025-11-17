package controller;

public class ItemUseResult {
    private final boolean success;
    private final String message;

    public ItemUseResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

   
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}