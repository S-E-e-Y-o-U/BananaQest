package com.example.mynewgame_for_kaf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StaticinformationAboutGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staticinformation_about_game);

            ImageButton bt_back = findViewById(R.id.buttonback);
            bt_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StaticinformationAboutGame.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });


    }
}