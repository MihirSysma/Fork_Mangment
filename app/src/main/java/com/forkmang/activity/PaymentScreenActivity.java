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
import com.forkmang.data.RestoData;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
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
    RestoData RestroData;
    String totalpay;
    Context ctx = PaymentScreenActivity.this;
    StorePrefrence storePrefrence;
    String order_id,booking_id, payment_type,isbooktable,order_id_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        storePrefrence = new StorePrefrence(ctx);
        RelativeLayout relative_view_1 = findViewById(R.id.relative_view_1);
        RelativeLayout relative_view_2 = findViewById(R.id.relative_view_2);
        RadioButton radioButton_cash = findViewById(R.id.radioButton1);
        RadioButton radioButton_online = findViewById(R.id.radioButton2);
        Button btn_payment = findViewById(R.id.btn_payment);

        tableList_get = (TableList) getIntent().getSerializableExtra("model");
        RestroData = (RestoData) getIntent().getSerializableExtra("restromodel");
        totalpay = getIntent().getStringExtra("totalpay");
        isbooktable= getIntent().getStringExtra("isbooktable");

        if(isbooktable.equalsIgnoreCase("yes"))
        {
            booking_id= getIntent().getStringExtra("bookingid");
        }
        else if(isbooktable.equalsIgnoreCase("no")){
            order_id = getIntent().getStringExtra("orderid");
        }

        relative_view_1.setOnClickListener(v -> {
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2));
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            radioButton_cash.setTextColor(ContextCompat.getColor(this, R.color.white));
            radioButton_online.setTextColor(ContextCompat.getColor(this, R.color.black));

            radioButton_online.setChecked(false);
            radioButton_cash.setChecked(true);
        });

        relative_view_2.setOnClickListener(v -> {
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2));
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            radioButton_online.setTextColor(ContextCompat.getColor(this, R.color.white));
            radioButton_cash.setTextColor(ContextCompat.getColor(this, R.color.black));


            radioButton_cash.setChecked(false);
            radioButton_online.setChecked(true);
        });


        btn_payment.setText("Pay - "+ totalpay);

        btn_payment.setOnClickListener(v -> {

            if(radioButton_cash.isChecked())
            {
                payment_type="cash";
            }
            else if(radioButton_online.isChecked())
            {
                payment_type="online";
            }

            if (Utils.isNetworkAvailable(ctx)) {
                if(isbooktable.equalsIgnoreCase("yes"))
                {
                    //callApi_makepayment_1(order_id, payment_type);
                    callApi_makepayment("",booking_id, payment_type,"table");
                }
                else{
                    callApi_makepayment(order_id,storePrefrence.getString(Constant.BOOKINGID), payment_type,"order");
                }



            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void callApi_makepayment(String order_id, String booking_id, String payment_type, String order_type)
    {
        Api.getInfo().make_payment("Bearer "+storePrefrence.getString(TOKEN_LOGIN),"",booking_id, payment_type,"table").
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
                                    final Intent mainIntent = new Intent(PaymentScreenActivity.this, Order_ConformationActivity.class);
                                    mainIntent.putExtra("model",tableList_get);
                                    mainIntent.putExtra("restromodel", RestroData);
                                    mainIntent.putExtra("totalpay",totalpay);
                                    if(isbooktable.equalsIgnoreCase("yes"))
                                    {
                                        mainIntent.putExtra("orderid",booking_id);
                                    }
                                    else if(isbooktable.equalsIgnoreCase("no")){
                                        mainIntent.putExtra("orderid",order_id);
                                    }

                                    startActivity(mainIntent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

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

    /*public void callApi_makepayment(String order_id,String payment_type)
    {
        Api.getInfo().make_payment("Bearer "+storePrefrence.getString(TOKEN_LOGIN),order_id, payment_type).
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
                                    final Intent mainIntent = new Intent(PaymentScreenActivity.this, Order_ConformationActivity.class);
                                    mainIntent.putExtra("model",tableList_get);
                                    mainIntent.putExtra("restromodel", RestroData);
                                    mainIntent.putExtra("totalpay",totalpay);

                                    mainIntent.putExtra("orderid",order_id);
                                    startActivity(mainIntent);
                                }
                                else{
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

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
    }*/




}