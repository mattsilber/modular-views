package com.guardanis.modular.modules.animation;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.modular.modules.ViewModule;

public abstract class TimedAnimationModule<T extends View> extends AnimationModule<T> {

    private double durationMs;

    public TimedAnimationModule(long durationMs){
        if(durationMs < 1)
            throw new IllegalArgumentException("Duration must be > 0");

        this.durationMs = Long.valueOf(durationMs)
                .doubleValue();
    }

    @Override
    public void onAnimationUpdate(){
        double percentCompleted = Math.min(1, (System.currentTimeMillis() - animationStart) / durationMs);
        if(percentCompleted < 1)
            onAnimationUpdate(percentCompleted);
        else onAnimationCompleted();
    }

    public abstract void onAnimationUpdate(double percentCompleted);

}
