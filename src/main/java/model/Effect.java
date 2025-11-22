package model;
//Effect is a generic interface for things like status effects (poison, buffs, etc.) that can be applied to a warrior.
public interface Effect<T extends Iwarrior> {
   
    String apply(T target); 
    boolean isFinished();
    String getName();
    
    // Return the modified damage after this effect's influence
    // Each effect must decide if it modifies damage or not
    int modifyIncomingDamage(int incomingDamage);
}
