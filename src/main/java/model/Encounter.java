package model;


//   Marker interface for any entity that can be encountered in a level.
//   This includes enemies, traps, shops, or any future encounter types.
  
//   By using this interface, we can create type-safe Level<T extends Encounter>
//   that ensures levels only contain valid encounter types.
 
public interface Encounter {
    //String getName();
}
