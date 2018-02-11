package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;


public class SplashScreen extends Activity{

    RoundCornerProgressBar progress;
    ImageView logo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progress = (RoundCornerProgressBar) findViewById(R.id.progress);
        logo = (ImageView) findViewById(R.id.imageView2);
        logo.setBackgroundResource(0);
        logo.setBackgroundResource(R.drawable.finallogo);

        new CountDownTimer(1000, 1){
            public void onFinish() {
                progress.setProgress(10);
                progress.setSecondaryProgress(20);
            }
            public void onTick(long millisUntilFinished){

            }
        }.start();

        new CountDownTimer(2000, 1){
            public void onFinish() {
                progress.setProgress(30);
                progress.setSecondaryProgress(40);
            }
            public void onTick(long millisUntilFinished){

            }
        }.start();

        new CountDownTimer(3000, 1){
            public void onFinish() {
                progress.setProgress(50);
                progress.setSecondaryProgress(60);
            }
            public void onTick(long millisUntilFinished){

            }
        }.start();

        new CountDownTimer(4000, 1){
            public void onFinish() {
                progress.setProgress(70);
                progress.setSecondaryProgress(80);
            }
            public void onTick(long millisUntilFinished){

            }
        }.start();

        new CountDownTimer(4500, 1){
            public void onFinish() {
                progress.setProgress(99);
                progress.setSecondaryProgress(100);
                Intent i = new Intent(SplashScreen.this, MainMenu.class);
                startActivity(i);
            }
            public void onTick(long millisUntilFinished){

            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
