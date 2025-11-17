package model;
//more general
//this is class is like a blueprint for different type of items 
public abstract class Item {
    private final String name;
    private final String imagePath;
    private final String description;
    private final int cost; 

    public Item(String name, String description, String imagePath, int cost) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.cost = cost;
    }

   
    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public abstract void use(Player player);

    
    public boolean isConsumable() {
        return false;
    }

    
    public abstract String getUseMessage(Player user);

    public abstract String getStatsString();
}
