package com.netomix.marior_run;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView {
    final SurfaceHolder holder;
    final GameThread gameLoopThread;

    int velAltoTortuga=1;
    private double scale=1.75;
    private int tiempo =0,x=0,velx=20,vely=0,acel=20,y=0;
    int puntos=0;
    boolean aterrizado=true;
    private boolean salto = false;
    private boolean estaSaltando = false;
    private int alto;
    private int ancho;
    int numBoleque;
    int minDistancia=64;

    int maxDistancia=1000;
    int maxEnemigos=2;

    int altutaBloques=2;
    List<Entidad> entidadList;
    Context contextto;
    int alturaMaxBloques=0;
    int ciclo=500;
    int timeSalto=0;
    int maxSalto=20;
    int time=0;
    boolean penalitisalto=false;
    int enemigoArea=64;
    int velocidadEcebario=10;
    Bloque bloque;
    int posMarioY=0;
    Entidad mario;
    int altoMario=64;
    boolean inGame=true;
    boolean agachado=false;
    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                entidadList =new ArrayList<>();
                ancho=getWidth();
                alto=getHeight();
                maxDistancia=ancho;
                bloque=new Bloque(context,R.drawable.bloque2,0,0);
                bloque.resize(32,32);

                alturaMaxBloques= alto-(bloque.getAlto()*altutaBloques*2);
                System.out.println("Altura"+alturaMaxBloques);
                 posMarioY= (alturaMaxBloques+altoMario);
                int posMarioX=bloque.getAncho();
                 Sprite marios[] = new Sprite[6];
                marios[0]=new Sprite(context,R.drawable.mario1 ,32,64);
                marios[1]=new Sprite(context,R.drawable.mario2 ,32,64);
                marios[2]=new Sprite(context,R.drawable.mario3, 32,64);
                marios[3]=new Sprite(context,R.drawable.mario2 ,32,64);
                marios[4]=new Sprite(context, R.drawable.mario4 ,32,64);
                marios[5]=new Sprite(context, R.drawable.mario5 ,32,44);
                mario =new Entidad(marios,posMarioX,posMarioY);
                mario.setCollider(32,64);
                contextto=context;
                numBoleque=ancho/bloque.getAncho()+1;
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });

    }

    @Override
    public void draw(Canvas canvas) {
        time++;
        if (time%50==0){
            velocidadEcebario++;
        }
        if(time%10==5){
            maxDistancia--;
            if(maxDistancia<=enemigoArea){
                maxDistancia=enemigoArea+10;
            }
        }
        if(tiempo%2==0){
            puntos++;
        }
        if (time>=ciclo){
            time=0;
            maxEnemigos++;
            if (maxEnemigos>=10){
                maxEnemigos=10;
            }
        }
        super.draw(canvas);
        canvas.drawColor(Color.argb(255, 92, 148, 252));
       // canvas=dibujarDebug(canvas);
        for(int i=0;i<numBoleque;i++){
            for (int j=1;j<=altutaBloques;j++){
                canvas.drawBitmap(bloque.getBmp(),(int)(x+bloque.getAncho()*i),alto-bloque.getAlto()*j,null);
            }
        }
            if(salto&&aterrizado){
                y=mario.getPosY();
                aterrizado=false;
            }
        if(salto){
            vely += acel;
            y -= vely;
            if (vely>=50){
                salto=false;
                vely=0;
            }
            if(timeSalto>=maxSalto){
                salto=false;
                penalitisalto=true;
                vely=0;
                timeSalto=0;
            }else{
                timeSalto++;
            }
            mario.setPosY(y);
        }
        if(salto==false && aterrizado==false){
            vely += 9.8;
            y += vely;
            mario.setPosY(y);
        }
        if(mario.getPosY()>=alturaMaxBloques+altoMario){
            mario.setPosY(alturaMaxBloques+altoMario);
            aterrizado=true;
            timeSalto=0;
            penalitisalto=false;
        }
        if(inGame){
            if(!aterrizado){
                canvas=mario.dibujar(canvas,4);
            }else{
                if(agachado){
                    mario.setCollider(32,44);
                    mario.setPosY(posMarioY+16);
                    canvas=mario.dibujar(canvas,5);
                }else{
                    agachado=false;
                    mario.setPosY(posMarioY);
                    mario.setCollider(32,64);
                    canvas=mario.dibujar(canvas,tiempo%4);
                }
            }
        }else{
            maxDistancia=ancho;
            maxEnemigos=2;
            time=0;
            tiempo=0;
            timeSalto=0;
            penalitisalto=false;
            juegoTerminado(canvas);
        }

        canvas=mario.dibujarCollider(canvas);
        tiempo++;
        x-=velocidadEcebario;
        if(x<-bloque.getAncho()){
          x+= bloque.getAncho();
        }
        Random r=new Random();
        if(r.nextInt(100)>75){
            try {
                if(entidadList.size()<maxEnemigos){
                    if(entidadList.size()==0){
                        entidadList.add(  crearEnemigo(contextto));
                    }
                    else{
                            int distancia=r.nextInt(maxDistancia-minDistancia)+minDistancia;
                        if((entidadList.get(entidadList.size()-1).posX+distancia)<=ancho){
                            Entidad entidad = crearEnemigo(contextto);
                            entidadList.add(entidad);
                        }
                    }
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }

        for (Entidad elemento: entidadList){
            elemento.setPosX(elemento.getPosX()-velocidadEcebario);
            canvas=elemento.dibujar(canvas,tiempo%elemento.animacion.length);
            canvas=elemento.dibujarCollider(canvas);
            if(elemento.tag=="tortuga"){
                if(elemento.getPosY()>900){
                    elemento.setPosY(elemento.posY+velAltoTortuga);
                }
            }
            if(isCollider(elemento,mario)){
                inGame=false;
            }
        }
        if (entidadList.size()>0){
            if(entidadList.get(0).posX<=-100){
                entidadList.remove(0);
            }
        }
        canvas=dibujarPuntuacion(canvas);
    }
    public boolean onTouchEvent(MotionEvent event){
        if(inGame==false){
            inGame=true;
            entidadList=new ArrayList<>();
            puntos=0;
        }
        int action = event.getAction();
        float halfScreenHAncho = ancho / 2;
        float touchX = event.getX();
        System.out.println("X toque"+touchX);
        if (touchX < halfScreenHAncho) {
            if(penalitisalto==false){
                salto=true;
            }
        } else {
            if( mario.getPosY()>=alturaMaxBloques+altoMario-10){
                agachado=true;
            }
        }
        if(action==MotionEvent.ACTION_UP){
            agachado=false;
        }
        return true;
    }

    Entidad crearPipa(Context context){
        Sprite pipa[] = new Sprite[1];
        Random r=new Random( );
        Entidad entidad =null;
        int altoEntodad= 64;
        int num=r.nextInt(3);
        switch (num){
            case 0:
                altoEntodad=64;
                pipa[0]=new Sprite(context, R.drawable.tubo1 ,64,altoEntodad);
                entidad =new Entidad(pipa,ancho, (int) ( (alturaMaxBloques+altoEntodad )));
                entidad.setCollider(64,altoEntodad);
                break;
            case 1:
                altoEntodad= 96;
                pipa[0]=new Sprite(context,R.drawable.tubo2 ,64,altoEntodad);
                entidad =new Entidad(pipa,ancho, (int) ( (alturaMaxBloques +32 )));
                entidad.setCollider(64,altoEntodad);
                break;
            case 2:
                altoEntodad= 128;
                pipa[0]=new Sprite(context,R.drawable.tubo3 ,64,altoEntodad);
                entidad =new Entidad(pipa,ancho, (int) ( (alturaMaxBloques    )));
                entidad.setCollider(64,altoEntodad);
                break;
        }

        return entidad;
    }
    Entidad crearTotuga(Context context){
        Sprite tortuga[] = new Sprite[2];
        tortuga[0]=new Sprite(context,R.drawable.tortuga1 ,32,48);
        tortuga[1]=new Sprite(context,R.drawable.tortuga2 ,32,48);
        Entidad entidad =new Entidad(tortuga,ancho, (alturaMaxBloques-24));
        entidad.setCollider(32,48);
        entidad.tag="tortuga";
        return entidad;
    }

    Entidad crearEnemigo(Context context ){
        Random r=new Random();
        boolean cual=r.nextBoolean();
        if(cual){
            return  crearPipa(context);
        }
        else {
            return  crearTotuga(context);
        }
    }

    boolean isCollider(Entidad a,Entidad b){
        int x1 = a.getPosX()-a.getAncho()/2;
        int y1 =a.getPosY();
        int ancho1 =a.getAncho();
        int alto1 =a.getAlto();
        int x2 = b.getPosX();
        int y2 =b.getPosY();
        int ancho2 =b.getAncho();
        int alto2 =b.getAlto();
        int izquierda1 = x1;
        int derecha1 = x1 + ancho1;
        int arriba1 = y1;
        int abajo1 = y1 + alto1;
        int izquierda2 = x2;
        int derecha2 = x2 + ancho2;
        int arriba2 = y2;
        int abajo2 = y2 + alto2;
        boolean seSuperponen = izquierda1 < derecha2 &&
                derecha1 > izquierda2 &&
                arriba1 < abajo2 &&
                abajo1 > arriba2;
        return  seSuperponen;
    }

    Canvas dibujarDebug(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(42);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);

        canvas.drawText("Minimo distacia:"+minDistancia,100,80,paint);
        canvas.drawText("Maximo distacia:"+maxDistancia,100,120,paint);
        canvas.drawText("Maximo enemigos:"+maxEnemigos,600,80,paint);
        canvas.drawText("Enemigos actuales:"+entidadList.size(),600,160,paint);
        canvas.drawText("Velocidad enemigos:"+velocidadEcebario,600,120,paint);
        canvas.drawText("Posicion :"+mario.getPosX()+" "+mario.getPosY(),1200,80,paint);
        canvas.drawText("Aceleracion :"+acel,1200,120,paint);
        canvas.drawText("Y :"+y,1200,160,paint);
        canvas.drawText("Vel X :"+velx,1400,200,paint);
        canvas.drawText("Vel Y :"+vely,1200,200,paint);
        canvas.drawText("Puntaje :"+puntos,ancho-300,80,paint);
        canvas.drawText("Tiempo ciclo:"+time,100,160,paint);
        canvas.drawText("Tiempo:"+tiempo,100,200,paint);
        return canvas;
    }
    public void activarCollider(){

    }
    Canvas dibujarPuntuacion(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(42);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);
        String texto="Puntuacion: "+puntos;
        float anchoTexto = paint.measureText(texto);
        canvas.drawText(texto,ancho-anchoTexto,80,paint);

        return  canvas;
    }
    Canvas juegoTerminado(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(200);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);
        String texto="Juego Terminado";
        String texto2="Pulsa para reiniciar";
        float anchoTexto = paint.measureText(texto);
        float x = (canvas.getWidth() - anchoTexto) / 2;
        canvas.drawText(texto,x,(alto-200)/2,paint);
        paint.setTextSize(82);
        anchoTexto = paint.measureText(texto2);

        canvas.drawText(texto2,(int)((ancho- anchoTexto) / 2),((alto-200)/2)+182,paint);
        return  canvas;
    }
}

