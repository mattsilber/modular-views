package com.guardanis.modular;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

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
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        controller.onDrawDispatched(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(controller != null)
            controller.onDetachedFromWindow();
    }

    public ModularController<ModularView> getCollectionController(){
        return controller;
    }

    public ModularView registerModule(ViewModule<ModularView> module){
        controller.registerModule(module);

        return this;
    }

    public void unregisterModule(ViewModule<ModularView> module){
        controller.unregisterModule(module);
    }

    public <V extends ViewModule<ModularView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

}
