package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;
import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class Order_ConformationActivity extends AppCompatActivity {

    TableList tableList_get;
    RestoData restoData;
    RecyclerView recyclerView;
    TextView txt_totalPaidamt,txt_rule,txt_dresscode,txt_ocassion,txt_customername,txt_mobileno,txt_customer_add,txt_timeview,txt_indoor,txt_order_id;
    TextView txtrestroname, txt_endtime, txt_distance,txttotalkm;
    Context ctx = Order_ConformationActivity.this;
    StorePrefrence storePrefrence;
    String totalpay, order_id;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_seatreserve);
        storePrefrence = new StorePrefrence(ctx);
        progressbar = findViewById(R.id.progressbar);
        txt_order_id = findViewById(R.id.txt_order_id);
        txt_totalPaidamt = findViewById(R.id.txt_totalPaidamt);
        txtrestroname = findViewById(R.id.txt_hotelname);
        txt_endtime = findViewById(R.id.txt_endtime);
        txt_distance = findViewById(R.id.txt_distance);
        txttotalkm = findViewById(R.id.txt_totalkm);
        txt_timeview = findViewById(R.id.txt_timeview);
        txt_indoor = findViewById(R.id.txt_indoor);
        txt_customername = findViewById(R.id.txt_customername);
        txt_customer_add = findViewById(R.id.txt_customer_add);
        txt_mobileno = findViewById(R.id.txt_mobileno);
        txt_rule = findViewById(R.id.txt_rule);
        txt_dresscode = findViewById(R.id.txt_dresscode);
        txt_ocassion = findViewById(R.id.txt_ocassion);


        tableList_get = (TableList) getIntent().getSerializableExtra("model");
        restoData = (RestoData) getIntent().getSerializableExtra("restromodel");
        totalpay = getIntent().getStringExtra("totalpay");
        order_id = getIntent().getStringExtra("orderid");

        if (Utils.isNetworkAvailable(ctx)) {
            callApi_getOrderDetail(order_id);
        }
        else{
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
        }

        txt_order_id.setText("Order Id: "+order_id);
        //String data_total = Select_Food_Fragment.cartBookingArrayList.get(0).getData_total();
        txt_customername.setText(tableList_get.getStr_customer_name());
        txt_mobileno.setText(storePrefrence.getString(Constant.MOBILE));
        txt_indoor.setText(tableList_get.getNumber_of_person() + " " + "Seats");

    }

    public void callApi_getOrderDetail(String order_id)
    {
        progressbar.setVisibility(View.VISIBLE);
        Api.getInfo().get_orderdetail("Bearer "+storePrefrence.getString(TOKEN_LOGIN),order_id).
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
                                    JSONObject dataobj = jsonObject.getJSONObject("data");
                                    txt_rule.setText(dataobj.getJSONArray("booking_table").getJSONObject(0).getString("rules"));
                                    txt_dresscode.setText(dataobj.getJSONArray("booking_table").getJSONObject(0).getString("dresscode"));
                                    //txt_timeview.setText(tableList_get.getStr_time());
                                    txt_timeview.setText((dataobj.getJSONArray("booking_table").getJSONObject(0).getString("date")));
                                    txt_ocassion.setText((dataobj.getJSONArray("booking_table").getJSONObject(0).getString("occasion")));


                                    txtrestroname.setText(dataobj.getJSONObject("restaurant").getString("rest_name"));
                                    txt_endtime.setText("Branch Name: "+dataobj.getJSONObject("restaurant").getString("rest_branch"));
                                    txt_distance.setText("ContactNo: "+dataobj.getJSONObject("restaurant").getString("contact"));

                                    txttotalkm.setText(restoData.getDistance()+" Km");
                                    txt_totalPaidamt.setText(ctx.getResources().getString(R.string.rupee)+dataobj.getString("total"));
                                }
                                else{
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }

                                progressbar.setVisibility(View.GONE);

                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        progressbar.setVisibility(View.GONE);
                    }
                });
    }
}