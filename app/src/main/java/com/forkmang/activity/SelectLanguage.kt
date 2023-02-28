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
        BtnArabic.setOnClickListener { v: View? ->
            val langPref: String = "Language"
            val prefs: SharedPreferences = getSharedPreferences(
                "CommonPrefs",
                MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(langPref, "ar")
            editor.commit()
            GotoNextScreen()
        }
        BtnEnglish.setOnClickListener { v: View? ->
            val langPref: String = "Language"
            val prefs: SharedPreferences = getSharedPreferences(
                "CommonPrefs",
                MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString(langPref, "en")
            editor.commit()
            GotoNextScreen()
        }
    }

    private fun GotoNextScreen() {
        val mainIntent: Intent = Intent(this@SelectLanguage, LoginActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}