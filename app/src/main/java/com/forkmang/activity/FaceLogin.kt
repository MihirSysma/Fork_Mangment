package com.forkmang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R

class FaceLogin constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_login)
        val img_faceid: ImageView = findViewById(R.id.img_faceid)
        val BtnReg: Button = findViewById(R.id.BtnReg)
        BtnReg.setOnClickListener { v: View? ->
            val mainIntent: Intent = Intent(this@FaceLogin, RegisterActivity::class.java)
            startActivity(mainIntent)
        }
        img_faceid.setOnClickListener { v: View? ->
            val mainIntent: Intent = Intent(this@FaceLogin, FaceLoginPermission::class.java)
            startActivity(mainIntent)
        }
    }
}