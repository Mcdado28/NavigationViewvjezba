package com.example.adismurtic.navigationviewvjezba;

/**
 * Created by adis.murtic on 30/06/2015.
 */

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;


public class Splashscreena extends Activity  {



    // Varijable koje odredjuju trajanje Splash Screena i napredak Progress Bara
    public static final int SEKUNDE = 5;
    public static final int MILISEKUNDE = SEKUNDE * 1000;
    public static final int DELAY = 2;

    private ProgressBar progressBar;

    // Pokreni Splash Screen Aktivnost
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Animiraj sliku koja se pojavljuje na Splash Screenu
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        ImageView splashImage = (ImageView) findViewById(R.id.splashimage);
        splashImage.startAnimation(animation);

        // Pokreni Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(max_progress());

        // Pokreni nasu malu stopericu
        tokvremena();
    }

    // Ova funkcija odredjuje trajanje naseg Splash Screena
    // Nakon odbrojanog vremena pokrece nasu glavnu aktivnost (Main Activity)
    public void tokvremena() {

        new CountDownTimer(MILISEKUNDE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                // Progress Bar vraca trenutni napredak u ucitavanju
                progressBar.setProgress(progress(millisUntilFinished));

            }

            @Override
            public void onFinish() {

                // Nakon isteka vremena startaj glavnu aktivnost
                Intent startapp = new Intent(Splashscreena.this, MainActivity.class);
                startActivity(startapp);
                finish();

            }

        }.start();

    }

    // Trenutni napredak u Progress Baru
    public int progress(long miliseconds) {

        return (int) (MILISEKUNDE - miliseconds) / 1000;

    }

    // Vraca maximalnu vrijednost Progress Bara
    public int max_progress() {

        return SEKUNDE - DELAY;

    }



}