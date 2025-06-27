package com.example.mynewgame_for_kaf;

import android.content.Context;

public class Sprite extends Thread{

    private final Context context;

    public Sprite(Context context) {
        this.context = context.getApplicationContext(); // Используем ApplicationContext
    }
    public static int num_for_bitmap_player = 1;
    public static int num_for_bitmap_Enemey = 0;
    static boolean running = false;
    public void requestStop() {
        running = false;
        interrupt(); // Прерываем sleep
    }

    @Override
    public void run() {
        while (running) {
            if (context != null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                num_for_bitmap_player++;
                num_for_bitmap_Enemey++;

                if (num_for_bitmap_player == 4) {
                    num_for_bitmap_player = 1;
                }
                if (num_for_bitmap_Enemey == 3) {
                    num_for_bitmap_Enemey = 1;
                }
            }
        }
        }
}
