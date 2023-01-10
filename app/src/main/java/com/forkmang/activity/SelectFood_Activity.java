package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter_SelectFood;
import com.forkmang.data.BookTable;
import com.forkmang.data.FoodList_Tab;
import com.forkmang.fragment.Select_Food_Fragment;
import com.forkmang.helper.Constant;
import com.forkmang.helper.Utils;
import com.forkmang.models.TableList;
import com.forkmang.network_call.Api;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFood_Activity extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Button btn_view_cart;
    Context ctx = SelectFood_Activity.this;
    ArrayList<FoodList_Tab> foodListArrayList;
    BookTable bookTable;
    TableList tableList;
    String booking_id;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectfood);
        viewPager=findViewById(R.id.viewPager);
        progressBar=findViewById(R.id.progressBar);
        tabLayout=findViewById(R.id.tabLayout);
        btn_view_cart = findViewById(R.id.btn_view_cart);

        TextView txtrestroname = findViewById(R.id.txtrestroname);
        TextView txt_time = findViewById(R.id.txt_time);
        TextView txt_distance = findViewById(R.id.txt_distance);
        TextView txt_totalkm = findViewById(R.id.txt_totalkm);


        bookTable = (BookTable) getIntent().getSerializableExtra("bookTable_model");
        tableList = (TableList) getIntent().getSerializableExtra("table_model");

        txtrestroname.setText(bookTable.getRest_name());
        txt_time.setText(bookTable.getEndtime());
        txt_totalkm.setText(bookTable.getDistance()+" km");
        booking_id = bookTable.getId();


        btn_view_cart.setOnClickListener(v -> {
            //showAlertView();
            Select_Food_Fragment all_Food_fragment = Select_Food_Fragment.GetInstance();
            all_Food_fragment.cartListingView();
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("pageno", ""+position);
                FoodList_Tab foodList_tab = foodListArrayList.get(position);
                String category_id = foodList_tab.getId();

                Select_Food_Fragment all_Food_fragment = Select_Food_Fragment.GetInstance();
                all_Food_fragment.callApi_food_1(category_id,booking_id);

            }

        });

        if (Utils.isNetworkAvailable(ctx)) {
            callapi_tablisting("1");
        }
        else{
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

        }

    }

    private void fill_tablist() {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {

            for(int i = 0; i<foodListArrayList.size(); i++)
            {
                FoodList_Tab foodList_tab = foodListArrayList.get(position);
                tab.setText(foodList_tab.getName());
            }
        }).attach();
    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectFood_Activity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablefood_reservealert, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_share_order;
        btn_share_order=dialogView.findViewById(R.id.btn_share_order);

        btn_share_order.setOnClickListener(v -> {

            Intent intent = new Intent(SelectFood_Activity.this, BookingOrder_ReserverConformationActivity.class);
            startActivity(intent);
            dialog.dismiss();

         });
        dialog.show();

    }


    private void callapi_tablisting(String branch_id)
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getres_foodlist(branch_id).
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
                                    if(jsonObject.getJSONObject("data").getJSONArray("data").length() > 0)
                                    {
                                        foodListArrayList = new ArrayList<>();
                                        for(int i =0; i< jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                        {
                                            FoodList_Tab foodList_tab = new FoodList_Tab();
                                            JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i);
                                            foodList_tab.setName(mjson_obj.getString("name"));
                                            foodList_tab.setId(mjson_obj.getString("id"));

                                            foodListArrayList.add(foodList_tab);
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        ViewPagerAdapter_SelectFood viewPagerAdapter_reserveSeat=new ViewPagerAdapter_SelectFood(getSupportFragmentManager(),getLifecycle(),foodListArrayList, tableList,bookTable);
                                        viewPager.setAdapter(viewPagerAdapter_reserveSeat);

                                        fill_tablist();
                                    }

                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        //stopProgress();

                    }
                });
    }




}