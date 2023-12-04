package com.example.admissionaceapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.admissionaceapplication.MainActivity;
import com.example.admissionaceapplication.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        ImageView bg = findViewById(R.id.imageView2);
        ConstraintLayout cons = findViewById(R.id.textContainer);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        bg.startAnimation(animation);
        cons.startAnimation(animation);
        MediaPlayer ring = MediaPlayer.create(SplashScreenActivity.this, R.raw.splash); ring.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}