package model;
//encapsulation
public abstract class Trap {
    protected String name;
    protected String triggerMessage;

    public Trap(String name, String triggerMessage) {
        this.name = name;
        this.triggerMessage = triggerMessage;
    }

    public String getName() {
        return name;
    }

    public String getTriggerMessage() {
        return triggerMessage;
    }

    public abstract String trigger(Player player);
}
