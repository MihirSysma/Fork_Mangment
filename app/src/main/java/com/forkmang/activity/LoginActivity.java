package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.forkmang.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button BtnLogin = findViewById(R.id.BtnLogin);
        Button BtnReg = findViewById(R.id.BtnReg);

        BtnReg.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
            //finish();
        });


        BtnLogin.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginActivity.this, LoginFormActivity.class);
            startActivity(mainIntent);
            //finish();
        });
    }
}