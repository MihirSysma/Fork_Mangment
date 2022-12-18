package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.forkmang.R;

public class LoginFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        Button BtnReg = findViewById(R.id.BtnReg);
        Button BtnLogin = findViewById(R.id.BtnLogin);

        BtnLogin.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginFormActivity.this, DashBoardActivity_2.class);
            startActivity(mainIntent);
        });

        txtForgotPassword.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginFormActivity.this, ForgotPassword.class);
            startActivity(mainIntent);
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            //finish();
        });

        BtnReg.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginFormActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
            finish();
        });



    }
}