package com.example.gps_tracker_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Work extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.your_song);
        mediaPlayer.start();

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mediaPlayer.stop();

                Intent intent = new Intent(getBaseContext(), Map.class);
                intent.putExtra("list", getIntent().getSerializableExtra("list"));
                startActivity(intent);
            }
        });
    }
}
