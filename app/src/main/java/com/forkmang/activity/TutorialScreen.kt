package com.forkmang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R


class TutorialScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_screen)
        val txtSkip: TextView = findViewById(R.id.txtSkip)
        txtSkip.setOnClickListener {
            val mainIntent: Intent = Intent(this@TutorialScreen, SelectLanguage::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}