package com.netomix.marior_run;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bloque {
    int posX=0;
    int posY=0;
    int alto=0;
    int ancho=0;
    Bitmap bmp;
    double SCALE=2;
    public Bloque(Context context,int id, int posX, int posY ) {
        this.bmp = BitmapFactory.decodeResource(context.getResources(),id);
        this.posX = posX;
        this.posY = posY;
    }
    public  void resize(int x,int y){
        this.bmp = Bitmap.createScaledBitmap(  this.bmp, (int)(x*SCALE), (int)(x*SCALE), true);
        this.alto=(int)(y*SCALE);
        this.ancho=(int)(x*SCALE);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
}
