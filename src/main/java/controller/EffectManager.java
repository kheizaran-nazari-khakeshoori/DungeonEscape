package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Effect;


//EffectManager is a helper class that manages a collection of effects.


public class EffectManager<T> {
    private final List<Effect<T>> activeEffects;
    
    public EffectManager() {
        this.activeEffects = new ArrayList<>();
    }
    
    public void addEffect(Effect<T> effect) {
        activeEffects.add(effect);
    }
    

    public boolean hasEffect(String effectName) {
        for (Effect<T> effect : activeEffects) {
            if (effect.getName().equals(effectName)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeEffect(String effectName) {
        Iterator<Effect<T>> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            if (effect.getName().equals(effectName)) {
                iterator.remove();
                break;
            }
        }
    }
    
    public void removeEffectsOfType(Class<?> effectType) {
        Iterator<Effect<T>> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            if (effectType.isInstance(effect)) {
                iterator.remove();
            }
        }
    }
    
    //It applies ALL active effects to the target, removes finished effects, and returns a summary message. 
    public String applyAllEffects(T target) {
        if (activeEffects.isEmpty()) {
            return "";
        }
        
        StringBuilder effectsResult = new StringBuilder();
        Iterator<Effect<T>> iterator = activeEffects.iterator();
        
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            String result = effect.apply(target);
            
            if (result != null && !result.isEmpty()) {
                if (!effectsResult.isEmpty()) {
                    effectsResult.append("\n");
                }
                effectsResult.append(result);
            }
            
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        
        return effectsResult.toString();
    }
    
    public List<Effect<T>> getActiveEffects() {
        return new ArrayList<>(activeEffects);
    }
}