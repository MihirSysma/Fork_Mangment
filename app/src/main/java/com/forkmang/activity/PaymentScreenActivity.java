package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;
import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentScreenActivity extends AppCompatActivity {
    TableList tableList_get;
    BookTable bookTable;
    String totalpay;
    Context ctx = PaymentScreenActivity.this;
    StorePrefrence storePrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        storePrefrence = new StorePrefrence(ctx);
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

            //callApi_createorder();
            final Intent mainIntent = new Intent(PaymentScreenActivity.this, BookingSeat_ReserveConformationActivity.class);
            mainIntent.putExtra("model",tableList_get);
            mainIntent.putExtra("bookTable",bookTable);
            mainIntent.putExtra("totalpay",totalpay);
            startActivity(mainIntent);

        });

    }

    public void callApi_createorder()
    {
        Api.getInfo().create_order("Bearer "+storePrefrence.getString(TOKEN_LOGIN),bookTable.getRestaurant_id()).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE))
                                {

                                  /*
                                    final Intent mainIntent = new Intent(PaymentScreenActivity.this, BookingSeat_ReserveConformationActivity.class);
                                    mainIntent.putExtra("model",tableList_get);
                                    mainIntent.putExtra("bookTable",bookTable);
                                    mainIntent.putExtra("totalpay",totalpay);
                                    startActivity(mainIntent);
                                    */

                                }

                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();

                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();


                    }
                });
    }
}