package com.guardanis.modular.modules.animation.shatter;

import android.graphics.Rect;

import java.util.Random;

public class ShatterPiece {

    private static final int MAX_VALOCITY = 25;
    private static final int MIN_VALOCITY = 5;

    private int[] startingPosition;
    private int[] currentPosition;

    private int[] size;
    private int[] velocity;

    public ShatterPiece(Random random, int[] currentPosition, int[] size){
        this.startingPosition = new int[]{ currentPosition[0], currentPosition[1] };
        this.currentPosition = currentPosition;
        this.size = size;

        this.velocity = new int[]{ generateRandomVelocity(random),
                generateRandomVelocity(random) };
    }

    private int generateRandomVelocity(Random random){
        boolean negative = random.nextInt(2) == 1;
        return (random.nextInt(MAX_VALOCITY) + MIN_VALOCITY)
                * (negative ? -1 : 1);
    }

    public void update(){
        currentPosition[0] += velocity[0];
        currentPosition[1] += velocity[1];
    }

    public int[] getCurrentPosition(){
        return currentPosition;
    }

    public int[] getStartingPosition(){
        return startingPosition;
    }

    public int[] getSize(){
        return size;
    }

    public void fill(Rect rect){
        rect.left = currentPosition[0];
        rect.top = currentPosition[1];
        rect.right = currentPosition[0] + size[0];
        rect.bottom = currentPosition[1] + size[1];
    }
}
