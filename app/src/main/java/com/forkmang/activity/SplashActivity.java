package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.forkmang.R;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        loadLocale();
        GotoNextScreeen();

    }

    private void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }


    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }



    private void GotoNextScreeen()
    {
        new Handler().postDelayed(() -> {

             /*final Intent mainIntent = new Intent(SplashActivity.this, TutorialScreen.class);
             startActivity(mainIntent);
             finish();*/

            final Intent mainIntent = new Intent(SplashActivity.this, DashBoardActivity_2.class);
            startActivity(mainIntent);
            finish();

            /*final Intent mainIntent = new Intent(SplashActivity.this, Booking_TabView_Activity.class);
            startActivity(mainIntent);
            finish();*/

        }, 2000);
    }

}