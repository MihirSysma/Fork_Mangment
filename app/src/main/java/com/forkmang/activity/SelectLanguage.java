package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.forkmang.R;

public class SelectLanguage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        Button BtnEnglish = findViewById(R.id.BtnEnglish);
        Button BtnArabic  = findViewById(R.id.BtnArabic);

        BtnArabic.setOnClickListener(v -> {
            String langPref = "Language";
            SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                    SelectLanguage.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(langPref, "ar");
            editor.commit();

            GotoNextScreen();

        });

        BtnEnglish.setOnClickListener(v -> {
            String langPref = "Language";
            SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                    SelectLanguage.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(langPref, "en");
            editor.commit();

            GotoNextScreen();
        });
    }

    private void GotoNextScreen()
    {
        final Intent mainIntent = new Intent(SelectLanguage.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}