package com.example.mynewgame_for_kaf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private int number = 0; // Изменили на нестатическое поле

    public static boolean musMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ImageButton button_for_play = findViewById(R.id.play_button);
        button_for_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DrawThread.class);
                startActivity(i);
            }
        });

        ImageButton button_for_settings = findViewById(R.id.button_settings);
        button_for_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
            }
        });

        ImageButton button_for_info = findViewById(R.id.button_info);
        button_for_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StaticinformationAboutGame.class);
                startActivity(i);
            }
        });

    }
}