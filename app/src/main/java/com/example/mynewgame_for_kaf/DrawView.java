package com.example.mynewgame_for_kaf;


import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    private static DrawingThread drawThread;

    public void cleanup() {
        if (drawThread != null) {
            drawThread.requestStop();
        }
    }

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawingThread(getContext(),getHolder());
        drawThread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DrawingThread.setFlying(250);
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // изменение размеров SurfaceView
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
        Intent i = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(i);
    }

}
