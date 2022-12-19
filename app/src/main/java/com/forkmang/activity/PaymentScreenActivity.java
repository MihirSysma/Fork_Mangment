package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.forkmang.R;

public class PaymentScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        RelativeLayout relative_view_1 = findViewById(R.id.relative_view_1);
        RelativeLayout relative_view_2 = findViewById(R.id.relative_view_2);
        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);


        relative_view_1.setOnClickListener(v -> {
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2));
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            radioButton1.setTextColor(ContextCompat.getColor(this, R.color.white));
            radioButton2.setTextColor(ContextCompat.getColor(this, R.color.black));

            radioButton2.setChecked(false);
            radioButton1.setChecked(true);
        });

        relative_view_2.setOnClickListener(v -> {
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2));
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            radioButton2.setTextColor(ContextCompat.getColor(this, R.color.white));
            radioButton1.setTextColor(ContextCompat.getColor(this, R.color.black));


            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
        });

        Button btn_payment_done = findViewById(R.id.btn_payment_done);
        btn_payment_done.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(PaymentScreenActivity.this, BookingSeat_ReserveConformationActivity.class);
            startActivity(mainIntent);
        });

    }
}