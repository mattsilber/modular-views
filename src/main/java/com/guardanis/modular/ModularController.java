package com.guardanis.modular;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.modular.modules.ViewModule;
import com.guardanis.modular.modules.animation.AnimationModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularController<T extends View> implements View.OnTouchListener {

    protected Map<String, ViewModule<T>> viewModules = new HashMap<String, ViewModule<T>>();
    protected Map<String, AnimationModule<T>> animationModules = new HashMap<String, AnimationModule<T>>();

    protected T parent;

    protected List<Class> drawingPrioritiesOrder = new ArrayList<Class>();
    protected boolean drawingEnabled = true;

    public ModularController(T parent){
        this.parent = parent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for(String key : viewModules.keySet())
            if(safelyDelegateTouchEvent(viewModules.get(key), v, event))
                return true;

        for(String key : animationModules.keySet())
            if(safelyDelegateTouchEvent(animationModules.get(key), v, event))
                return true;

        return false;
    }

    private boolean safelyDelegateTouchEvent(ViewModule module, View v, MotionEvent event){
        try{
            if(module.onTouch(v, event))
                return true;
        }
        catch(Throwable e){ e.printStackTrace(); }

        return false;
    }

    public ModularController<T> registerModule(ViewModule<T> module){
        unregisterModule(module.getClass());

        module.setParent(parent);

        if(module instanceof AnimationModule)
            animationModules.put(module.getClass().getName(), (AnimationModule) module);
        else viewModules.put(module.getClass().getName(), module);

        return this;
    }

    public <V extends ViewModule<T>> void unregisterModule(Class<V> moduleClass){
        ViewModule module = getModule(moduleClass);
        if(module != null){
            module.onDetachedFromWindow();

            viewModules.remove(moduleClass.getName());
            animationModules.remove(moduleClass.getName());
        }
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

        if(!isDrawingBlockedByAnimation()){
            drawPriorityModules(canvas);
            drawViewModules(canvas);
        }

        drawAnimatingModules(canvas);
    }

    protected void drawPriorityModules(Canvas canvas){
        if(drawingPrioritiesOrder == null)
            return;

        ViewModule module;
        for(Class key : drawingPrioritiesOrder){
            module = getModule(key);

            if(module != null && module.isDrawingEnabled()){
                if(module instanceof AnimationModule){
                    if(((AnimationModule) module).isAnimating())
                        drawModuleSafely(canvas, module);
                }
                else drawModuleSafely(canvas, module);
            }
        }
    }

    protected void drawViewModules(Canvas canvas){
        for(String key : viewModules.keySet())
            if(!drawingPrioritiesOrder.contains(viewModules.get(key).getClass()) && viewModules.get(key).isDrawingEnabled())
                drawModuleSafely(canvas, viewModules.get(key));
    }

    protected void drawAnimatingModules(Canvas canvas){
        for(String key : animationModules.keySet())
            if(!drawingPrioritiesOrder.contains(animationModules.get(key).getClass()) && animationModules.get(key).isAnimating())
                 drawModuleSafely(canvas, animationModules.get(key));
    }

    protected boolean isDrawingBlockedByAnimation(){
        for(String key : animationModules.keySet())
            if(animationModules.get(key).isAnimating() && !animationModules.get(key).isParentDrawAllowed())
                return true;

        return false;
    }

    public boolean isAnimating(){
        for(String key : animationModules.keySet())
            if(animationModules.get(key).isAnimating())
                return true;

        return false;
    }

    protected void drawModuleSafely(Canvas canvas, ViewModule module){
        try{
            module.draw(canvas);
        }
        catch(Throwable e){ e.printStackTrace(); }
    }

    public boolean isSuperDrawingAllowed(){
        return !isDrawingBlockedByAnimation();
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

    public ModularController<T> setDrawingPrioritiesOrder(List<Class> drawingPrioritiesOrder){
        this.drawingPrioritiesOrder = drawingPrioritiesOrder;

        return this;
    }

    public ModularController<T> setDrawingPrioritiesOrder(Class[] drawingPriorities){
        this.drawingPrioritiesOrder = new ArrayList<Class>();

        for(Class c : drawingPriorities)
            this.drawingPrioritiesOrder.add(c);

        return this;
    }
}
