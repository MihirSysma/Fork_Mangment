package com.forkmang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.forkmang.R;

public class WalkinInQuee extends AppCompatActivity {

    TextView txt_order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkin_in_quee);
        txt_order_id = findViewById(R.id.txt_order_id);

        txt_order_id.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(WalkinInQuee.this, Activity_PaymentSummary_WalkinFragment.class);
            startActivity(mainIntent);

        });


    }

    
}