package com.guardanis.modular;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewModule<T extends View> implements View.OnTouchListener {

    protected T parent;

    protected boolean drawingEnabled = true;

    public ViewModule<T> setParent(T parent){
        this.parent = parent;

        return this;
    }

    public abstract void draw(Canvas canvas);

    public void onDetachedFromWindow(){
        this.parent = null;
    }

    public boolean isDrawingEnabled() {
        return drawingEnabled;
    }

    public void setDrawingEnabled(boolean drawingEnabled) {
        this.drawingEnabled = drawingEnabled;
    }
}
