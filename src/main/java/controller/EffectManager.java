package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Effect;
import model.Iwarrior;

//EffectManager is a helper class that manages a collection of effects.


public class EffectManager<T extends Iwarrior> {
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
    
    //remove all the effects of one type in the list of activeeffect 
    public void removeEffectsOfType(Class<?> effectType) {
        Iterator<Effect<T>> iterator = activeEffects.iterator();//do not forget that iterator is a tool for  interating through a collection like list 
        while (iterator.hasNext()) {
            Effect<T> effect = iterator.next();
            if (effectType.isInstance(effect)) {
                iterator.remove();
            }
        }
    }
     
    public List<Effect<T>> getActiveEffects() {
        return new ArrayList<>(activeEffects);
    }
}