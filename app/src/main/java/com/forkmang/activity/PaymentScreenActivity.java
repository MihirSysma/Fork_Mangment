package com.forkmang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.forkmang.R;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;

public class PaymentScreenActivity extends AppCompatActivity {


    TableList tableList_get;
    BookTable bookTable;
    String totalpay;
    Context ctx = PaymentScreenActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        RelativeLayout relative_view_1 = findViewById(R.id.relative_view_1);
        RelativeLayout relative_view_2 = findViewById(R.id.relative_view_2);
        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        Button btn_payment = findViewById(R.id.btn_payment);

        tableList_get = (TableList) getIntent().getSerializableExtra("model");
        bookTable = (BookTable) getIntent().getSerializableExtra("bookTable");
        totalpay = getIntent().getStringExtra("totalpay");



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


        btn_payment.setText("Pay - "+ totalpay);

        btn_payment.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(PaymentScreenActivity.this, BookingSeat_ReserveConformationActivity.class);
            mainIntent.putExtra("model",tableList_get);
            mainIntent.putExtra("bookTable",bookTable);
            startActivity(mainIntent);
        });

    }
}