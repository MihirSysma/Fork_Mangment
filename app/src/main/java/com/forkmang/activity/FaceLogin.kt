package com.forkmang.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R

class FaceLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_login)

        val imgFaceId: ImageView = findViewById(R.id.img_faceid)
        val btnReg: Button = findViewById(R.id.BtnReg)
        btnReg.setOnClickListener {
            val mainIntent = Intent(this@FaceLogin, RegisterActivity::class.java)
            startActivity(mainIntent)
        }
        imgFaceId.setOnClickListener {
            val mainIntent = Intent(this@FaceLogin, FaceLoginPermission::class.java)
            startActivity(mainIntent)
        }
    }
}