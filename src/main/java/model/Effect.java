package model;

//type saftey , reusable code , one code used by different type 
//so it demonstrates generic polymorphism 
//this T can  be any type , player , enemy ,,,, 
//open close principle , i can add burneffect for example 




public interface Effect<T> {
   
    String apply(T target); // in enemy it can be apply (enemy target)

    
    boolean isFinished();

   
    String getName();
}

// why not use <T extends icombatant>??
//for maxixmum flexibility , like a non-player character 
//it is a focused interface >> so i have interface segregation 
