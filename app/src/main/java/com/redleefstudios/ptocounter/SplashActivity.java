package com.redleefstudios.ptocounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private final static String TUTORIAL_COMPLETED = "TUTORIAL_COMPLETED_BOOLEAN";
    private boolean tutorialComplete;

    private int vacationTotal, sickTotal, otherTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash_screen);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tutorialComplete = prefs.getBoolean(TUTORIAL_COMPLETED, false);
        vacationTotal = prefs.getInt(MainActivity.VACATION_TOTAL_SAVE_NAME, 15);
        sickTotal = prefs.getInt(MainActivity.SICK_TOTAL_SAVE_NAME, 10);
        otherTotal = prefs.getInt(MainActivity.OTHER_TOTAL_SAVE_NAME, 0);
        tutorialComplete=false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Check for first run or not
                if (tutorialComplete) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Intent i = new Intent(SplashActivity.this, TutorialActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    prefs.edit().putBoolean(TUTORIAL_COMPLETED, true).commit();
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
