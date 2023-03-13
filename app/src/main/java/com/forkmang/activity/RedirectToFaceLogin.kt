package com.forkmang.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R


class RedirectToFaceLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirecttofacelogin)
        gotoNextScreen()
    }

    private fun gotoNextScreen() {
        Handler().postDelayed({
            val mainIntent = Intent(this@RedirectToFaceLogin, FaceLogin::class.java)
            startActivity(mainIntent)
            finish()
        }, 2000)
    }
}