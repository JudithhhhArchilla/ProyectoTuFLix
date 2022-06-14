package com.example.proyectotuflix;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class Animacion extends AppCompatActivity {

    public static int SPLASH_SCREEM = 2500;
    private ImageView tuflixLogo;
    private ObjectAnimator objectAnimatorFuntion;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animacion);
        tuflixLogo = findViewById(R.id.tuflixLogo);
        runAnimations();


        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Animacion.this, MainActivity.class);
                startActivity(intent);
            }
        };
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, SPLASH_SCREEM);
    }

    public void runAnimations() {
        loadAnimations(tuflixLogo, "alpha", 1f, 0f, 1500, 1000);
    }


    public void loadAnimations(Object imageView, String propertyName, float valueA, float valueB, int startDelay, int duration) {
        objectAnimatorFuntion = ObjectAnimator.ofFloat(imageView, propertyName, valueA, valueB);
        objectAnimatorFuntion.setStartDelay(startDelay);
        objectAnimatorFuntion.setDuration(duration);
        objectAnimatorFuntion.start();
    }


}