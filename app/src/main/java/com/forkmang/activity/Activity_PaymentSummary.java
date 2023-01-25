package com.forkmang.activity;

import static com.forkmang.helper.Constant.MOBILE;
import static com.forkmang.helper.Constant.SUCCESS_CODE;
import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.CartListingAdapter_Summary;
import com.forkmang.data.RestoData;
import com.forkmang.data.CartBooking;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.models.TableList;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_PaymentSummary extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btn_payment_proceed;
    LinearLayout lyt_arabic, lyt_eng,linear_1,linear_view_layout_2;
    TableList tableList_get;
    RestoData restoData;
    StorePrefrence storePrefrence;
    Context ctx = Activity_PaymentSummary.this;
    TextView txt_totalPay;
    ProgressBar progressBar;
    ArrayList<CartBooking> cartBookingArrayList;
    String coming_from;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_view);
        storePrefrence = new StorePrefrence(ctx);
        linear_1 = findViewById(R.id.linear_1);
        linear_view_layout_2 = findViewById(R.id.linear_view_layout_2);

        progressBar = findViewById(R.id.progressBar);
        TextView txt_phoneno = findViewById(R.id.txt_phoneno);
        TextView txt_date_time = findViewById(R.id.txt_date_time);
        TextView txt_noofseat = findViewById(R.id.txt_noofseat);
        TextView txt_hotelname = findViewById(R.id.txt_hotelname);
        TextView txt_customername = findViewById(R.id.txt_customername);
        txt_totalPay = findViewById(R.id.txt_totalPay);
       //ArrayList<CartBooking> cartBookingArrayList  = extras.getParcelableArrayList("cartbookingarraylist");

        coming_from = getIntent().getStringExtra("comingfrom");

        if(coming_from.equalsIgnoreCase("SelectFood"))
        {
            tableList_get = (TableList) getIntent().getSerializableExtra("model");
            txt_hotelname.setText(tableList_get.getStr_hotel_name());
            txt_customername.setText(tableList_get.getStr_customer_name());
            txt_noofseat.setText(tableList_get.getNumber_of_person() + " Seats");
            txt_date_time.setText(tableList_get.getStr_time());
        }
        else if(coming_from.equalsIgnoreCase("PickupFood"))
        {
            txt_hotelname.setText(restoData.getRest_name());
            txt_customername.setText(storePrefrence.getString(Constant.NAME));
            linear_1.setVisibility(View.GONE);
            linear_view_layout_2.setVisibility(View.GONE);


        }
        restoData = (RestoData) getIntent().getSerializableExtra("restromodel");
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
            if (Utils.isNetworkAvailable(ctx)) {
                if(coming_from.equalsIgnoreCase("SelectFood"))
                {
                    callApi_createorder();
                }
                else if(coming_from.equalsIgnoreCase("PickupFood"))
                {
                    callApi_createorder_pickup();
                }

            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
            }

        });

        progressBar.setVisibility(View.GONE);

        if (Utils.isNetworkAvailable(ctx)) {
            callApi_cartListview();
        }
        else{
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

        }

    }

    public void callApi_cartListview()
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getcart_detail("Bearer "+storePrefrence.getString(TOKEN_LOGIN), storePrefrence.getString(Constant.IDENTFIER)).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    JSONArray cart_array_item = data_obj.getJSONArray("cart_item");
                                    cartBookingArrayList = new ArrayList<>();
                                    for(int i = 0; i<cart_array_item.length(); i++)
                                    {
                                        CartBooking cartBooking = new CartBooking();

                                        JSONObject cart_detail_obj = cart_array_item.getJSONObject(i);


                                        //data obj
                                        cartBooking.setData_userid(data_obj.getString("user_id"));

                                        if(data_obj.has("booking_table_id"))
                                        {
                                            cartBooking.setData_booking_table_id(data_obj.getString("booking_table_id"));
                                        }
                                        else{
                                            cartBooking.setData_booking_table_id("");
                                        }


                                        cartBooking.setData_total(data_obj.getString("total"));

                                        //cart_item obj
                                        cartBooking.setCart_item_qty(cart_detail_obj.getString("qty"));
                                        cartBooking.setCart_item_cartid(cart_detail_obj.getString("cart_id"));
                                        cartBooking.setCart_item_id(cart_detail_obj.getString("id"));
                                        cartBooking.setCart_item_extra_id(cart_detail_obj.getString("item_extra_id"));

                                        //cart_item_details obj
                                        cartBooking.setCart_item_details_category_id(cart_detail_obj.getJSONObject("cart_item_details").getString("category_id"));
                                        cartBooking.setCart_item_details_name(cart_detail_obj.getJSONObject("cart_item_details").getString("name"));
                                        cartBooking.setCart_item_details_price(cart_detail_obj.getJSONObject("cart_item_details").getString("price"));


                                        //extra_item_details obj
                                        ArrayList<String>extra_namelist = new ArrayList<>();
                                        ArrayList<String>extra_pricelist = new ArrayList<>();
                                        for(int j = 0; j<cart_detail_obj.getJSONArray("extra_item_details").length(); j++)
                                        {
                                            JSONObject extra_item_obj = cart_detail_obj.getJSONArray("extra_item_details").getJSONObject(j);

                                            extra_namelist.add(extra_item_obj.getString("name"));
                                            extra_pricelist.add(extra_item_obj.getString("price"));


                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }

                                        //extra name
                                        String str_extraname ="";
                                        for(int k =0; k<extra_namelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraname = extra_namelist.get(k); }
                                            else{  str_extraname = str_extraname+","+extra_namelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_name(str_extraname);

                                        //extra price
                                        String str_extraprice ="";
                                        for(int k =0; k<extra_pricelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraprice = extra_pricelist.get(k); }
                                            else{  str_extraprice = str_extraprice+","+extra_pricelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_price(str_extraprice);
                                        cartBookingArrayList.add(cartBooking);

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    String data_total = cartBookingArrayList.get(0).getData_total();
                                    txt_totalPay.setText(ctx.getResources().getString(R.string.rupee)+data_total);

                                    //call adapter
                                    CartListingAdapter_Summary cartListingAdapter_summary = new CartListingAdapter_Summary(ctx,cartBookingArrayList);
                                    recyclerView.setAdapter(cartListingAdapter_summary);

                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                /*JSONObject obj = new JSONObject(loadJSONFromAsset_t());

                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    JSONArray cart_array_item = data_obj.getJSONArray("cart_item");
                                    cartBookingArrayList = new ArrayList<>();
                                    for(int i = 0; i<cart_array_item.length(); i++)
                                    {
                                        CartBooking cartBooking = new CartBooking();
                                        JSONObject cart_detail_obj = cart_array_item.getJSONObject(i);


                                        //data obj
                                        cartBooking.setData_userid(data_obj.getString("user_id"));
                                        cartBooking.setData_booking_table_id(data_obj.getString("booking_table_id"));
                                        cartBooking.setData_total(data_obj.getString("total"));

                                        //cart_item obj
                                        cartBooking.setCart_item_qty(cart_detail_obj.getString("qty"));
                                        cartBooking.setCart_item_cartid(cart_detail_obj.getString("cart_id"));
                                        cartBooking.setCart_item_id(cart_detail_obj.getString("item_id"));
                                        cartBooking.setCart_item_extra_id(cart_detail_obj.getString("item_extra_id"));

                                        //cart_item_details obj
                                        cartBooking.setCart_item_details_category_id(cart_detail_obj.getJSONObject("cart_item_details").getString("category_id"));
                                        cartBooking.setCart_item_details_name(cart_detail_obj.getJSONObject("cart_item_details").getString("name"));
                                        cartBooking.setCart_item_details_price(cart_detail_obj.getJSONObject("cart_item_details").getString("price"));


                                        //extra_item_details obj
                                        ArrayList<String>extra_namelist = new ArrayList<>();
                                        ArrayList<String>extra_pricelist = new ArrayList<>();
                                        for(int j = 0; j<cart_detail_obj.getJSONArray("extra_item_details").length(); j++)
                                        {
                                            JSONObject extra_item_obj = cart_detail_obj.getJSONArray("extra_item_details").getJSONObject(j);

                                            extra_namelist.add(extra_item_obj.getString("name"));
                                            extra_pricelist.add(extra_item_obj.getString("price"));


                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }

                                        //extra name
                                        String str_extraname ="";
                                        for(int k =0; k<extra_namelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraname = extra_namelist.get(k); }
                                            else{  str_extraname = str_extraname+","+extra_namelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_name(str_extraname);

                                        //extra price
                                        String str_extraprice ="";
                                        for(int k =0; k<extra_pricelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraprice = extra_pricelist.get(k); }
                                            else{  str_extraprice = str_extraprice+","+extra_pricelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_price(str_extraprice);
                                        cartBookingArrayList.add(cartBooking);

                                   }
                                    progressBar.setVisibility(View.GONE);
                                    //call adapter
                                    CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(getActivity(),cartBookingArrayList);
                                    recycleView.setAdapter(cartBookingAdapter);

                                }*/
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        //stopProgress();

                    }
                });
    }


    public void callApi_addqty(String cart_itemid,String qty)
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().cart_updateqty("Bearer "+storePrefrence.getString(TOKEN_LOGIN),cart_itemid, qty,storePrefrence.getString(Constant.IDENTFIER)).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    Toast.makeText(ctx, obj.getString("message"),Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    if (Utils.isNetworkAvailable(ctx)) {
                                        callApi_cartListview();
                                    }
                                    else{
                                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                                     }

                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void callApi_removeitemcart(String cart_itemid)
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().cart_removeqty("Bearer "+storePrefrence.getString(TOKEN_LOGIN),cart_itemid,storePrefrence.getString(Constant.IDENTFIER)).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    Toast.makeText(ctx, obj.getString("message"),Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    if (Utils.isNetworkAvailable(ctx)) {
                                        callApi_cartListview();
                                    }
                                    else{
                                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();

                    }
                });
    }


    public void callApi_createorder()
    {
        Api.getInfo().create_order("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                        restoData.getId(),"book_table","",storePrefrence.getString(Constant.BOOKINGID),storePrefrence.getString(Constant.IDENTFIER)).
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
                                    String order_id = jsonObject.getString("data");

                                    //Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    AlertDialog dialog =  showAlertView_conformtable();

                                    new Handler().postDelayed(() -> {
                                        dialog.dismiss();
                                        final Intent mainIntent = new Intent(Activity_PaymentSummary.this, PaymentScreenActivity.class);

                                        if(coming_from.equalsIgnoreCase("SelectFood"))
                                        {
                                            mainIntent.putExtra("model",tableList_get);
                                        }
                                        else if(coming_from.equalsIgnoreCase("PickupFood"))
                                        {
                                            //nothing to send table object
                                        }
                                        mainIntent.putExtra("comingfrom",coming_from);
                                        mainIntent.putExtra("restromodel", restoData);
                                        mainIntent.putExtra("totalpay",txt_totalPay.getText().toString());
                                        mainIntent.putExtra("orderid",order_id);
                                        mainIntent.putExtra("isbooktable","no");
                                        startActivity(mainIntent);
                                        //finish();
                                    }, 1000);
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
                            else if(response.code() == Constant.GUESTUSERlOGIN)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ctx,LoginActivity.class);
                                startActivity(intent);
                                finish();

                                //Toast.makeText(ctx,"You are guest user please login",Toast.LENGTH_SHORT).show();
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


    public void callApi_createorder_pickup()
    {
        Api.getInfo().create_order("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                        restoData.getId(),"pickup","","",storePrefrence.getString(Constant.IDENTFIER)).
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
                                    String order_id = jsonObject.getString("data");
                                    //Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    AlertDialog dialog =  showAlertView_conformtable();

                                    new Handler().postDelayed(() -> {
                                        dialog.dismiss();
                                        final Intent mainIntent = new Intent(Activity_PaymentSummary.this, PaymentScreenActivity.class);
                                        mainIntent.putExtra("model",tableList_get);
                                        mainIntent.putExtra("restromodel", restoData);
                                        mainIntent.putExtra("totalpay",txt_totalPay.getText().toString());
                                        mainIntent.putExtra("orderid",order_id);
                                        mainIntent.putExtra("isbooktable","no");

                                        startActivity(mainIntent);
                                        //finish();
                                    }, 1000);
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
                            else if(response.code() == Constant.GUESTUSERlOGIN)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ctx,LoginActivity.class);
                                startActivity(intent);
                                finish();

                                //Toast.makeText(ctx,"You are guest user please login",Toast.LENGTH_SHORT).show();
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

    private String loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        return language;

    }


    private AlertDialog showAlertView_conformtable()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_PaymentSummary.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablereserve_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();

        dialog.show();

        return  dialog;

    }

}