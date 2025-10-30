package model;

public interface IOperation_on_Effect<T>{
    void addEffect(Effect<T>effect);
    boolean hasEffect(String effectName);
    void removeEffect(String effectName);
    void removeEffectsOfType(Class<?> effectType);

    
}
