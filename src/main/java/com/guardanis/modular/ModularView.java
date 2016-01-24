package com.guardanis.modular;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.guardanis.modular.modules.ViewModule;

public class ModularView extends View {

    protected ModularController<ModularView> controller;

    public ModularView(Context context) {
        super(context);
        init();
    }

    public ModularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new ModularController<ModularView>(this);

        setOnTouchListener(controller);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(controller.isSuperDrawingAllowed())
            super.onDraw(canvas);

        controller.draw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(controller != null)
            controller.onDetachedFromWindow();
    }

    public ModularController<ModularView> getModularController(){
        return controller;
    }

    public ModularView registerModule(ViewModule<ModularView> module){
        controller.registerModule(module);

        return this;
    }

    public <V extends ViewModule<ModularView>> void unregisterModule(Class<V> moduleClass){
        controller.unregisterModule(moduleClass);
    }

    public <V extends ViewModule<ModularView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

    public void setModularDrawingEnabled(boolean enabled){
        controller.setDrawingEnabled(enabled);
    }

}
