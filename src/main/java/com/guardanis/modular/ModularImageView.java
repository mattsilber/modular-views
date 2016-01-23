package com.guardanis.modular;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.guardanis.modular.modules.ViewModule;

public class ModularImageView extends ImageView {

    protected ModularController<ModularImageView> controller;

    public ModularImageView(Context context) {
        super(context);
        init();
    }

    public ModularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init(){
        controller = new ModularController<ModularImageView>(this);

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

    public ModularController<ModularImageView> getCollectionController(){
        return controller;
    }

    public ModularImageView registerModule(ViewModule<ModularImageView> module){
        controller.registerModule(module);

        return this;
    }

    public <V extends ViewModule<ModularImageView>> void unregisterModule(Class<V> moduleClass){
        controller.unregisterModule(moduleClass);
    }

    public <V extends ViewModule<ModularImageView>> V getModule(Class<V> moduleClass){
        return controller.getModule(moduleClass);
    }

}
