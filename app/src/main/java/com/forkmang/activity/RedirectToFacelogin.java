package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.forkmang.R;

public class RedirectToFacelogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirecttofacelogin);
        GotoNextScreeen();
    }

    private void GotoNextScreeen()
    {
        new Handler().postDelayed(() -> {
            final Intent mainIntent = new Intent(RedirectToFacelogin.this, FaceLogin.class);
            startActivity(mainIntent);
            finish();
        }, 2000);
    }
}