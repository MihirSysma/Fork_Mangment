package com.forkmang.activity;

import static com.forkmang.helper.Constant.MOBILE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.CartBookingAdapter;
import com.forkmang.fragment.Select_Food_Fragment;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;

public class Activity_PaymentSummary extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btn_payment_proceed;
    LinearLayout lyt_arabic, lyt_eng;
    TableList tableList_get;
    BookTable bookTable;
    StorePrefrence storePrefrence;
    Context ctx = Activity_PaymentSummary.this;
    TextView txt_totalPay;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_view);
        storePrefrence = new StorePrefrence(ctx);
        progressBar = findViewById(R.id.progressBar);
        TextView txt_phoneno = findViewById(R.id.txt_phoneno);
        TextView txt_date_time = findViewById(R.id.txt_date_time);
        TextView txt_noofseat = findViewById(R.id.txt_noofseat);
        TextView txt_hotelname = findViewById(R.id.txt_hotelname);
        TextView txt_customername = findViewById(R.id.txt_customername);
        txt_totalPay = findViewById(R.id.txt_totalPay);



        //ArrayList<CartBooking> cartBookingArrayList  = extras.getParcelableArrayList("cartbookingarraylist");
        tableList_get = (TableList) getIntent().getSerializableExtra("model");
        bookTable = (BookTable) getIntent().getSerializableExtra("bookTable");

        txt_hotelname.setText(tableList_get.getStr_hotel_name());
        txt_customername.setText(tableList_get.getStr_customer_name());
        txt_noofseat.setText(tableList_get.getNumber_of_person() + " Seats");
        txt_date_time.setText(tableList_get.getStr_time());
        txt_phoneno.setText(storePrefrence.getString(MOBILE));


        progressBar.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycleview);
        btn_payment_proceed = findViewById(R.id.btn_payment_proceed);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_PaymentSummary.this));
        lyt_arabic = findViewById(R.id.lyt_arabic);
        lyt_eng = findViewById(R.id.lyt_eng);


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
            final Intent mainIntent = new Intent(Activity_PaymentSummary.this, PaymentScreenActivity.class);
            mainIntent.putExtra("model",tableList_get);
            mainIntent.putExtra("bookTable",bookTable);
            mainIntent.putExtra("totalpay",txt_totalPay.getText().toString());
            startActivity(mainIntent);
        });

        progressBar.setVisibility(View.GONE);


        CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(Activity_PaymentSummary.this, Select_Food_Fragment.cartBookingArrayList);
        recyclerView.setAdapter(cartBookingAdapter);

        String data_total = Select_Food_Fragment.cartBookingArrayList.get(0).getData_total();
        txt_totalPay.setText(ctx.getResources().getString(R.string.rupee)+data_total);




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