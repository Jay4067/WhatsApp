package com.learning.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();


        Thread thread = new Thread(){

            public void run() {
                try {

                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.fade_anim);
                    image.startAnimation(animation1);

                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {


                    Intent intent = new Intent(Splash_Screen.this,MainActivity.class);
                    startActivity(intent);
                }
            }

        };thread.start();
    }
}