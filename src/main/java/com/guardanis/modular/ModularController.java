package com.guardanis.modular;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.guardanis.modular.modules.animation.AnimationModule;

import java.util.HashMap;
import java.util.Map;

public class ModularController<T extends View> implements View.OnTouchListener {

    private Map<String, ViewModule<T>> viewModules = new HashMap<String, ViewModule<T>>();
    private Map<String, AnimationModule<T>> animationModules = new HashMap<String, AnimationModule<T>>();

    private T parent;
    private AnimationModule currentAnimationModule;

    private boolean drawingEnabled = true;

    public ModularController(T parent){
        this.parent = parent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for(String key : viewModules.keySet())
            if(viewModules.get(key).onTouch(v, event))
                return true;

        return false;
    }

    public ModularController<T> registerModule(ViewModule<T> module){
        module.setParent(parent);

        if(module instanceof AnimationModule)
            animationModules.put(module.getClass().getName(), (AnimationModule) module);
        else viewModules.put(module.getClass().getName(), module);

        return this;
    }

    public void unregisterModule(ViewModule<T> module){
        viewModules.remove(module.getClass().getName());
        animationModules.remove(module.getClass().getName());
    }

    public <V extends ViewModule<T>> V getModule(Class<V> moduleClass){
        ViewModule module = viewModules.get(moduleClass.getName());

        if(module == null)
            module = animationModules.get(moduleClass.getName());

        return module == null ? null : (V) module;
    }

    public void draw(Canvas canvas){
        if(!drawingEnabled)
            return;

        currentAnimationModule = getCurrentlyAnimatingModule();
        if(currentAnimationModule == null || currentAnimationModule.isParentDrawAllowed())
            drawModules(canvas);

        if(currentAnimationModule != null)
            currentAnimationModule.draw(canvas);
    }

    protected void drawModules(Canvas canvas){
        for(String key : viewModules.keySet())
            if(viewModules.get(key).isDrawingEnabled())
                viewModules.get(key).draw(canvas);
    }

    protected AnimationModule<T> getCurrentlyAnimatingModule(){
        for(String key : animationModules.keySet())
            if(animationModules.get(key).isAnimating())
                return animationModules.get(key);

        return null;
    }

    public boolean isSuperDrawingAllowed(){
        AnimationModule module = getCurrentlyAnimatingModule();
        return module == null || module.isParentDrawAllowed();
    }

    public void onDetachedFromWindow(){
        for(String key : viewModules.keySet())
            viewModules.get(key).onDetachedFromWindow();

        for(String key : animationModules.keySet())
            animationModules.get(key).onDetachedFromWindow();
    }

    public boolean isDrawingEnabled() {
        return drawingEnabled;
    }

    public void setDrawingEnabled(boolean drawingEnabled) {
        this.drawingEnabled = drawingEnabled;
    }
}
