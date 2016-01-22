package com.guardanis.modular.tools;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;

public class ViewHelper {

    @SuppressLint("NewApi")
    public static void disableHardwareAcceleration(View v) {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public static Bitmap getDrawingCache(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bmap = view.getDrawingCache();
        Bitmap snapshot = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(), bmap.getHeight(), null, true);
        view.setDrawingCacheEnabled(false);
        return snapshot;
    }

}
