package controller;

public class FleeResult {
    private final boolean success;
    private final String logMessage;

    public FleeResult(boolean success, String logMessage) {
        this.success = success;
        this.logMessage = logMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getLogMessage() {
        return logMessage;
    }
}