package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Effect;

/**
 * EffectManager is a helper class that manages a collection of effects.
 * It can be reused by both Player and Enemy classes.
 * 
 * @param <T> The type of target the effects apply to (Player or Enemy)
 */
public class EffectManager<T> {
    private final List<Effect<T>> activeEffects;
    
    public EffectManager() {
        this.activeEffects = new ArrayList<>();
    }
    
    /**
     * Adds an effect to the active effects list
     */
    public void addEffect(Effect<T> effect) {
        activeEffects.add(effect);
    }
    
    /**
     * Checks if an effect with the given name is currently active
     */
    public boolean hasEffect(String effectName) {
        for (Effect<T> effect : activeEffects) {
            if (effect.getName().equals(effectName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes an effect with the given name
     */
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
    
    /**
     * Removes all effects of a specific type (e.g., all PoisonEffect instances)
     */
    public void removeEffectsOfType(Class<?> effectType) {
        Iterator<Effect<T>> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            if (effectType.isInstance(effect)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * Applies all active effects to the target and removes finished effects
     * @param target The entity (Player or Enemy) to apply effects to
     * @return A message describing what effects were applied
     */
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
                if (effectsResult.length() > 0) {
                    effectsResult.append("\n");
                }
                effectsResult.append(result);
            }
            
            // Remove effect if it's finished
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        
        return effectsResult.toString();
    }
    
    /**
     * Returns a copy of the active effects list (for read-only access)
     */
    public List<Effect<T>> getActiveEffects() {
        return new ArrayList<>(activeEffects);
    }
}