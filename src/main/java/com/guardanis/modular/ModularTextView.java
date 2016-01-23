package com.guardanis.modular;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.guardanis.modular.modules.ViewModule;

public class ModularTextView extends TextView {

    protected ModularController<ModularTextView> controller;

    public ModularTextView(Context context) {
        super(context);
        init();
    }

    public ModularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new ModularController<ModularTextView>(this);

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

    public ModularController<ModularTextView> getCollectionController(){
        return controller;
    }

    public ModularTextView registerModule(ViewModule<ModularTextView> module){
        controller.registerModule(module);

        return this;
    }

    public <V extends ViewModule<ModularTextView>> void unregisterModule(Class<V> moduleClass){
        controller.unregisterModule(moduleClass);
    }

    public <V extends ViewModule<ModularTextView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

}
