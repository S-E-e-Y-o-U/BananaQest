package com.example.mynewgame_for_kaf;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DrawThread extends AppCompatActivity {
    public static FrameLayout fl_surfaceview_container;
    private Sprite new_sprite;
    private DrawView videoSurface;

    public static MediaPlayer mus_for_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_draw_thread);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (MainActivity.musMode){
            mus_for_game = MediaPlayer.create(this,R.raw.mus_for_game);
            mus_for_game.start();
        }

        fl_surfaceview_container = findViewById(R.id.fragment_file_videoplayer_surface_container);
        // Инициализация DrawView с правильным контекстом
        videoSurface = new DrawView(this); // this теперь валидный контекст

        // Инициализация Sprite
        new_sprite = new Sprite(getApplicationContext()); // Передаем контекст

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fl_surfaceview_container.addView(videoSurface);
        new_sprite.start();


        FrameLayout container = findViewById(R.id.fragment_file_videoplayer_surface_container);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        ImageButton buttonBack = new ImageButton(this);
        buttonBack.setId(View.generateViewId());
        buttonBack.setImageResource(R.drawable.button_back);
        buttonBack.setVisibility(View.INVISIBLE);
        buttonBack.setBackground(null);
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = 20;
        params.leftMargin = 260;
            container.addView(buttonBack, params);

        buttonBack.setOnClickListener(v -> {
            if (MainActivity.musMode){
                mus_for_game.stop();
            }
            Intent i = new Intent(DrawThread.this, MainActivity.class);
            startActivity(i);
            finish(); // Закрываем текущую активность
        });

        ImageButton buttonPause = new ImageButton(this);
        buttonPause.setId(View.generateViewId());
        buttonPause.setImageResource(R.drawable.button_pause);
        buttonPause.setBackground(null);

        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = 20;
        params.leftMargin = 20;

        container.addView(buttonPause, params);

        buttonPause.setOnClickListener(v -> {
            DrawingThread.timeFreeze();
            if (buttonBack.getVisibility() == View.VISIBLE) {
                buttonBack.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction(() -> buttonBack.setVisibility(View.INVISIBLE))
                        .start();
            } else {
                // Показываем с анимацией
                buttonBack.setVisibility(View.VISIBLE);
                buttonBack.setAlpha(0f);
                buttonBack.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start();
            }
        });

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


    }



}