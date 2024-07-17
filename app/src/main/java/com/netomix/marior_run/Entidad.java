package com.netomix.marior_run;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

public class Entidad {
    Sprite animacion[];
    String tag="";
    int posX,posY;
    int alto;
    int ancho;
    static double SCALE=2;
    public Entidad(Sprite animation[], int x, int y){
        animacion=animation;
        posX=x;
        posY=y;
    }
    public void setCollider(int ancho, int alto){
        this.alto=(int)(alto*SCALE);
        this.ancho=(int)(ancho*SCALE);
    }
    public Sprite[] getAnimacion() {
        return animacion;
    }

    public void setAnimacion(Sprite[] animacion) {
        this.animacion = animacion;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {


        this.posX = posX;
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

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Canvas dibujar(Canvas canvas, int frame){
        canvas.drawBitmap(this.animacion[frame ].getBmp(),this.getPosX()-this.getAncho()/2,this.getPosY()-this.getAlto()/2,null);
        return canvas;
    }
    public Canvas dibujar(Canvas canvas, int frame,int x, int y){
        canvas.drawBitmap(this.animacion[frame ].getBmp(),x-this.getAncho()/2,y-this.getAlto()/2,null);
        return canvas;
    }
    public Canvas dibujarCollider(Canvas canvas){
        Paint brocha ;
        brocha = new Paint();
        brocha.setColor(Color.GREEN);
        brocha.setTypeface(Typeface.DEFAULT);
        brocha.setAntiAlias(true);
        Point[]p=getCollider();
        canvas.drawLine( p[0].x, p[0].y, p[1].x, p[1].y,brocha);
        canvas.drawLine(p[1].x, p[1].y, p[2].x, p[2].y,brocha);
        canvas.drawLine(p[2].x, p[2].y, p[3].x, p[3].y,brocha);
        canvas.drawLine(p[3].x, p[3].y, p[0].x, p[0].y,brocha);
        return  canvas;
    }
    Point[] getCollider( ){
        Point[]p=new Point[4];
        p[0]=new Point(this.getPosX() - this.getAncho()/2,this.getPosY()-this.getAlto()/2);
        p[1]=new Point(this.getPosX() + this.getAncho()/2,this.getPosY()-this.getAlto()/2);
        p[2]=new Point(this.getPosX() + this.getAncho()/2,this.getPosY()+this.getAlto()/2);
        p[3]=new Point(this.getPosX() - this.getAncho()/2,this.getPosY()+this.getAlto()/2);
        return  p;
    }

}
