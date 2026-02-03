package model;

import java.util.List;

public interface IEffectable<T extends Iwarrior> {
    void addEffect(Effect<T> effect);
    boolean hasEffect(String effectName);
    void removeEffect(String effectName);
    void removeEffectsOfType(Class<? extends Effect<T>> effectType);
    List<Effect<T>> getActiveEffects();
    
}
