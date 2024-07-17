package com.netomix.marior_run;

import android.graphics.Canvas;

public class GameThread extends Thread{
    static final long FPS = 20;
    final GameView gameView;
    private boolean running = false;

    public GameThread(GameView view) { this.gameView = view; }

    public void setRunning(boolean run) { running = run; }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    gameView.draw(c);
                }
            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {

            }
        }
    }
}
