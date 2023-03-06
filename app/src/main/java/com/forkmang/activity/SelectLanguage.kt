package com.forkmang.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R


class SelectLanguage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language)

        val BtnEnglish: Button = findViewById(R.id.BtnEnglish)
        val BtnArabic: Button = findViewById(R.id.BtnArabic)
        BtnArabic.setOnClickListener {
            val langPref = "Language"
            val prefs: SharedPreferences = getSharedPreferences(
                "CommonPrefs",
                MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(langPref, "ar")
            editor.apply()
            GotoNextScreen()
        }

        BtnEnglish.setOnClickListener {
            val langPref = "Language"
            val prefs: SharedPreferences = getSharedPreferences(
                "CommonPrefs",
                MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(langPref, "en")
            editor.apply()
            GotoNextScreen()
        }
    }

    private fun GotoNextScreen() {
        val mainIntent = Intent(this@SelectLanguage, LoginActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}