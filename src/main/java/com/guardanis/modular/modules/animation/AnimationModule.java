package com.guardanis.modular.modules.animation;

import android.view.MotionEvent;
import android.view.View;

import com.guardanis.modular.modules.ViewModule;

public abstract class AnimationModule<T extends View> extends ViewModule<T> implements Runnable {

    public interface AnimationEventListener {
        public void onAnimationComplete();
    }

    protected static final int FRAME_SLEEP = 25;

    protected long animationStart;
    protected boolean animating = false;
    protected boolean animationUpdateAllowed = false;

    protected boolean parentDrawAllowed = true;

    protected AnimationEventListener animationEventListener;

    public void start(AnimationEventListener animationEventListener) {
        this.animationEventListener = animationEventListener;

        start();
    }

    public void start() {
        animationStart = System.currentTimeMillis();
        animating = true;
        animationUpdateAllowed = true;

        new Thread(this)
                .start();
    }

    @Override
    public void run() {
        final Long animationKey = Long.valueOf(animationStart);

        while(animationStart == animationKey && animating && animationUpdateAllowed && parent != null){
            try{
                onAnimationUpdate();

                parent.postInvalidate();

                Thread.sleep(FRAME_SLEEP);
            }
            catch(Throwable e){
                e.printStackTrace();

                if(animationKey == animationStart){
                    animationStart = 0;
                    onAnimationCompleted();
                }
            }
        }
    }

    protected abstract void onAnimationUpdate();

    protected void onAnimationCompleted() {
        this.animationUpdateAllowed = false;

        if(parent == null)
            animating = false;
        else parent.post(new Runnable(){
            public void run(){
                animating = false;

                if(animationEventListener != null)
                    animationEventListener.onAnimationComplete();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public boolean isAnimating() {
        return animating;
    }

    public boolean isParentDrawAllowed() {
        return parentDrawAllowed;
    }

    public AnimationModule<T> setParentDrawAllowed(boolean parentDrawAllowed) {
        this.parentDrawAllowed = parentDrawAllowed;
        return this;
    }

    @Override
    public void onDetachedFromWindow() {
        this.animating = false;

        super.onDetachedFromWindow();
    }
}
