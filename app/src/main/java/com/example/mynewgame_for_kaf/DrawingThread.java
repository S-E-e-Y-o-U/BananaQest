package com.example.mynewgame_for_kaf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;

import java.util.Random;

public class DrawingThread extends Thread {
        private SurfaceHolder surfaceHolder;
        public static boolean running = true;
        private Context context;

        static boolean time = true;
    private static final int SPRITE_WIDTH = 130;
    private static final int SPRITE_HEIGHT = 180;

    // Прямоугольники для проверки столкновений
    private Rect playerRect = new Rect();
    private Rect enemyRect = new Rect();
    private Rect bananaRect = new Rect();

        private Bitmap BackGroundBit;
        private Bitmap[] PlayerBit = new Bitmap[3];
        private Bitmap[] EnemyBit = new Bitmap[2];
        private Bitmap[] HealthBit = new Bitmap[3];

        private Bitmap BananaBit;

        private int Person_SPEED = 25;
        private static int PlayerY;
        private int EnemyX;
        private int EnemyY;

        private int BananaX;
        private int BananaY;

        private int score;
        private int health;

        private void RestartGame(){

        }

        public static void setFlying(int y){
            PlayerY = PlayerY - y;
        }

        public DrawingThread(Context context, SurfaceHolder surfaceHolder) {

            PlayerBit[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_1);
            PlayerBit[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_2);
            PlayerBit[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_3);

            EnemyBit[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey_1);
            EnemyBit[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey_2);

            HealthBit[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.helth1);
            HealthBit[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.helth2);
            HealthBit[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.helth3);

            BananaBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.banana);

            BackGroundBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgroundgame);

            this.context = context;
            this.surfaceHolder = surfaceHolder;
        }

        public static void requestStop() {
            running = true;
        }

        public static void timeFreeze(){
            time = !time;
        }

    private void respawnEnemy(){
            Random random = new Random();
            EnemyY = random.nextInt(1701); // От 0 до 1700 включительно
            EnemyX = 1000; // Или другое начальное значение по Y
        }

    private void respawnBanana(){
        Random random = new Random();
        BananaY = random.nextInt(1701); // От 0 до 1700 включительно
        BananaX = 1000; // Или другое начальное значение по Y
    }

        @Override
        public void run() {
            respawnEnemy();
            respawnBanana();
            health = 3;

            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(Color.WHITE);
            p.setTextSize(55.0f);

            while (running) {

                if (time) {
                    if (PlayerY < 0) {
                        PlayerY = 0;
                    }
                    if (PlayerY > 1800) {
                        PlayerY = 1800;
                    }
                    EnemyX -= Person_SPEED;
                    BananaX -= Person_SPEED;
                    if (BananaX < -300) {
                        respawnBanana();
                    }
                    if (EnemyX < -300) {
                        respawnEnemy();
                    }
                    PlayerY += 10;
                    Canvas canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        try {
                            // Очистка canvas
                            Paint backgroundPaint = new Paint();
                            {
                                backgroundPaint.setColor(Color.BLACK);
                                backgroundPaint.setStyle(Paint.Style.FILL);
                            }
                            //   canvas.drawRect(0,0, 1400, 1400, backgroundPaint );

                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            canvas.drawBitmap(BackGroundBit, 0, 0, null);

                            canvas.drawBitmap(BananaBit, BananaX, BananaY, null);

                            if (Sprite.num_for_bitmap_player == 1) {
                                canvas.drawBitmap(PlayerBit[0], 0, PlayerY, null);
                            } else if (Sprite.num_for_bitmap_player == 2) {
                                canvas.drawBitmap(PlayerBit[1], 0, PlayerY, null);
                            } else {
                                canvas.drawBitmap(PlayerBit[2], 0, PlayerY, null);
                            }

                            if (Sprite.num_for_bitmap_Enemey == 1) {
                                canvas.drawBitmap(EnemyBit[0], EnemyX, EnemyY, null);
                            } else {
                                canvas.drawBitmap(EnemyBit[1], EnemyX, EnemyY, null);
                            }

                            if (health == 3) {
                                canvas.drawBitmap(HealthBit[2], 500, 0, null);
                            }
                            if (health == 2) {
                                canvas.drawBitmap(HealthBit[1], 500, 0, null);
                            }
                            if (health == 1) {
                                canvas.drawBitmap(HealthBit[0], 500, 0, null);
                            }

                            checkCollisions();

                            drawScore(canvas);

                            if (health == 0) {
                                running = false;
                                EndGame(canvas);
                            }

                        } finally {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                    // Задержка для контроля FPS
                    try {
                        Thread.sleep(10); // ~60 FPS
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    private void checkCollisions() {
        // Обновляем прямоугольники для текущих позиций
        playerRect.set(0, PlayerY, SPRITE_WIDTH, PlayerY + SPRITE_HEIGHT);
        enemyRect.set(EnemyX, EnemyY, EnemyX + SPRITE_WIDTH, EnemyY + SPRITE_HEIGHT);
        bananaRect.set(BananaX, BananaY, BananaX + SPRITE_WIDTH, BananaY + SPRITE_HEIGHT);

        // Столкновение игрока с врагом
        if (Rect.intersects(playerRect, enemyRect)) {
            handleEnemyCollision();
        }

        // Столкновение игрока с бананом
        if (Rect.intersects(playerRect, bananaRect)) {
            handleBananaCollision();
        }
    }

    private void EndGame(Canvas canvas){
        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(130f); // Размер текста
        scorePaint.setAntiAlias(true);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);

        String scoreText = "GAME OVER";

        float x = canvas.getWidth() / 2 - (scorePaint.measureText(scoreText) / 2);
        float y = canvas.getHeight() / 2 - 30; // Отступ сверху

        // Рисуем текст
        canvas.drawText(scoreText, x, y, scorePaint);
    }

    private void drawScore(Canvas canvas) {
            Paint scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(50f); // Размер текста
        scorePaint.setAntiAlias(true);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);

        String scoreText = "Score: " + score;
        // Вычисляем позицию текста (правый верхний угол с отступом)
        float x = canvas.getWidth() - scorePaint.measureText(scoreText) - 20;
        float y = 60; // Отступ сверху

        // Рисуем текст
        canvas.drawText(scoreText, x, y, scorePaint);
    }

    private void handleEnemyCollision() {
        respawnEnemy(); // Перемещаем врага
        health--;
    }

    private void handleBananaCollision() {

        respawnBanana(); // Перемещаем банан
        score+=50;
    }

}




