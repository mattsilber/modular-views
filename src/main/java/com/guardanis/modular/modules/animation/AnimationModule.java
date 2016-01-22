package com.guardanis.modular.modules.animation;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.modular.ViewModule;

public abstract class AnimationModule<T extends View> extends ViewModule<T> implements Runnable {

    private static final int FRAME_SLEEP = 25;

    protected long animationStart;
    protected boolean animating = false;

    protected boolean parentDrawAllowed = true;

    public void start(){
        animationStart = System.currentTimeMillis();
        animating = true;

        new Thread(this)
                .start();
    }

    @Override
    public void run() {
        final Long animationKey = Long.valueOf(animationStart);

        try{
            while(animationStart == animationKey && animating){
                onAnimationUpdate();

                parent.postInvalidate();

                Thread.sleep(FRAME_SLEEP);
            }
        }
        catch(InterruptedException e){ e.printStackTrace(); }
    }

    protected abstract void onAnimationUpdate();

    protected void onAnimationCompleted(){
        this.animating = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        return false;
    }

    public boolean isAnimating() {
        return animating;
    }

    public boolean isParentDrawAllowed() {
        return parentDrawAllowed;
    }

    public void setParentDrawAllowed(boolean parentDrawAllowed) {
        this.parentDrawAllowed = parentDrawAllowed;
    }
}
