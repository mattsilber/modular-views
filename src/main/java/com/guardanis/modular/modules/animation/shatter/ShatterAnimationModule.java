package com.guardanis.modular.modules.animation.shatter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.guardanis.modular.modules.animation.AnimationModule;
import com.guardanis.modular.tools.ViewHelper;

import java.util.Random;

public class ShatterAnimationModule<T extends View> extends AnimationModule<T> {

    private static final int ALPHA_UPDATE = 5;
    private static final int[] MAX_SIZE = new int[]{ 30, 40 };

    private ShatterPiece[][] pieces;
    private int[] gridSize;

    private Bitmap drawingCache;

    private Rect bitmapRect = new Rect();
    private Rect destinationRect = new Rect();

    private Paint alphaPaint = new Paint();

    public ShatterAnimationModule(){
        setParentDrawAllowed(false);
    }

    @Override
    public void start(){
        this.drawingCache = ViewHelper.getDrawingCache(parent);
        buildPieces();

        alphaPaint.setAntiAlias(true);
        alphaPaint.setAlpha(255);

        super.start();
    }

    private void buildPieces() {
        Random random = new Random();

        this.gridSize = calculateSize(drawingCache);
        this.pieces = new ShatterPiece[gridSize[0]][gridSize[1]];

        ShatterPiece p = null;

        for(int x = 0; x < gridSize[0]; x++){
            for(int y = 0; y < gridSize[1]; y++){
                p = new ShatterPiece(random,
                        new int[]{ x * MAX_SIZE[0], y * MAX_SIZE[1] },
                        MAX_SIZE);

                pieces[x][y] = p;
            }
        }
    }

    private int[] calculateSize(Bitmap drawingCache){
        return new int[]{ drawingCache.getWidth() / MAX_SIZE[0],
                drawingCache.getHeight() / MAX_SIZE[1] };
    }

    @Override
    protected void onAnimationUpdate() {
        if(pieces == null)
            return;

        for(int x = 0; x < gridSize[0]; x++)
            for(int y = 0; y < gridSize[1]; y++)
                pieces[x][y].update();

        int alpha = Math.max(0, alphaPaint.getAlpha() - ALPHA_UPDATE);
        alphaPaint.setAlpha(alpha);

        if(alpha == 0)
            onAnimationCompleted();
    }

    @Override
    public void draw(Canvas canvas) {
        if(pieces == null)
            return;

        ShatterPiece active;
        for(int x = 0; x < gridSize[0]; x++){
            for(int y = 0; y < gridSize[1]; y++){
                active = pieces[x][y];

                bitmapRect.left = MAX_SIZE[0] * x;
                bitmapRect.top = MAX_SIZE[1] * y;
                bitmapRect.right = bitmapRect.left + active.getSize()[0];
                bitmapRect.bottom = bitmapRect.top + active.getSize()[1];

                active.fill(destinationRect);

                canvas.drawBitmap(drawingCache, bitmapRect, destinationRect, alphaPaint);
            }
        }
    }

    @Override
    protected void onAnimationCompleted(){
        super.onAnimationCompleted();

        drawingCache = null;
        pieces = null;
    }

}
