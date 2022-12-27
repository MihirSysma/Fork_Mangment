package com.forkmang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;

public class LoginActivity extends AppCompatActivity {

    StorePrefrence storePrefrence;
    Context ctx = LoginActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button BtnLogin = findViewById(R.id.BtnLogin);
        Button BtnReg = findViewById(R.id.BtnReg);
        storePrefrence=new StorePrefrence(ctx);



        BtnReg.setOnClickListener(v -> {
            if(storePrefrence.getString(Constant.NAME).length() == 0)
            {
                final Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mainIntent);
                //finish();
            }
            else{
                Toast.makeText(ctx,"User already registered please click login", Toast.LENGTH_LONG).show();
            }
            //finish();
        });


        BtnLogin.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginActivity.this, LoginFormActivity.class);
            startActivity(mainIntent);
            //finish();
        });
    }
}