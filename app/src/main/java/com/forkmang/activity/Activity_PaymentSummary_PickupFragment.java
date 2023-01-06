package com.forkmang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.CartListingAdapter;

public class Activity_PaymentSummary_PickupFragment extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btn_payment_proceed;
    LinearLayout lyt_arabic, lyt_eng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_summary_pickup);
        recyclerView = findViewById(R.id.recycleview);
        btn_payment_proceed = findViewById(R.id.btn_payment_proceed);
        lyt_arabic = findViewById(R.id.lyt_arabic);
        lyt_eng = findViewById(R.id.lyt_eng);

        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_PaymentSummary_PickupFragment.this));

        if(loadLocale().equalsIgnoreCase("ar"))
        {
            lyt_eng.setVisibility(View.GONE);
            lyt_arabic.setVisibility(View.VISIBLE);
        }
        else
        {
            //english
            lyt_arabic.setVisibility(View.GONE);
            lyt_eng.setVisibility(View.VISIBLE);

        }




        btn_payment_proceed.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(Activity_PaymentSummary_PickupFragment.this, Pickup_ConformationActivity.class);
            startActivity(mainIntent);
        });

        CartListingAdapter cartBookingAdapter = new CartListingAdapter(Activity_PaymentSummary_PickupFragment.this );
        recyclerView.setAdapter(cartBookingAdapter);

    }

    private String loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        return language;

    }

}