package com.netomix.marior_run;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sprite {
    Bitmap bmp;
    double SCALE =2;
    public Sprite(Context context, int id ,int sizeX, int sizeY) {

        this.bmp = BitmapFactory.decodeResource(context.getResources(),id);
        this.bmp  = Bitmap.createScaledBitmap(  this.bmp , (int)(sizeX*SCALE), (int)(sizeY*SCALE), true);
    }
    public Bitmap getBmp() {
        return bmp;
    }
}
