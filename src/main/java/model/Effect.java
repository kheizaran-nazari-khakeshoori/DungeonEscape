package model;

public interface Effect<T> {
   
    String apply(T target); 
    boolean isFinished();
    String getName();
    
}

