package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.forkmang.R;

public class FaceLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);
        ImageView img_faceid = findViewById(R.id.img_faceid);
        Button BtnReg = findViewById(R.id.BtnReg);


        BtnReg.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(FaceLogin.this, RegisterActivity.class);
            startActivity(mainIntent);
            //finish();
        });

        img_faceid.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(FaceLogin.this, FaceLoginPermission.class);
            startActivity(mainIntent);
            //finish();
        });
    }
}