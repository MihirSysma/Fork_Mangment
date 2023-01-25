package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter_PickupSelectFood;
import com.forkmang.data.FoodList_Tab;
import com.forkmang.data.RestoData;
import com.forkmang.fragment.PickupSelect_Food_Fragment;
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

public class PickupSelectFood_Activity extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Button btn_view_cart;
    Context ctx = PickupSelectFood_Activity.this;
    ArrayList<FoodList_Tab> foodListArrayList;
    RestoData restoData;
    TableList tableList;
    String booking_id,category_id;
    ProgressBar progressBar;
    int current_tabactive;
    EditText etv_searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectfood);
        viewPager=findViewById(R.id.viewPager);
        progressBar=findViewById(R.id.progressBar);
        tabLayout=findViewById(R.id.tabLayout);

        //tabLayout.setupWithViewPager(viewPager);

        btn_view_cart = findViewById(R.id.btn_view_cart);
        LinearLayout linear_view_1 = findViewById(R.id.linear_view_1);
        LinearLayout linear_view_2 = findViewById(R.id.linear_view_2);
        LinearLayout linear_view_3 = findViewById(R.id.linear_view_3);
        linear_view_1.setVisibility(View.GONE);
        linear_view_2.setVisibility(View.GONE);
        linear_view_3.setVisibility(View.GONE);
        TextView txt_booktable = findViewById(R.id.txt_booktable);
        txt_booktable.setText("Pickup Table");

        ImageView img_searchicon = findViewById(R.id.img_searchicon);
        ImageView img_edit = findViewById(R.id.img_edit);
        etv_searchview = findViewById(R.id.etv_searchview);


        TextView txtrestroname = findViewById(R.id.txtrestroname);
        TextView txt_time = findViewById(R.id.txt_time);
        TextView txt_distance = findViewById(R.id.txt_distance);
        TextView txt_totalkm = findViewById(R.id.txt_totalkm);

        TextView txt_datetime = findViewById(R.id.txt_datetime);
        TextView txt_day = findViewById(R.id.txt_day);
        TextView txt_noofseat = findViewById(R.id.txt_noofseat);
        TextView txt_view_day = findViewById(R.id.txt_view_day);
        TextView txt_view_day_2 = findViewById(R.id.txt_view_day_2);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        restoData = (RestoData) getIntent().getSerializableExtra("restromodel");
        //tableList = (TableList) getIntent().getSerializableExtra("table_model");

        /*String timedate = getIntent().getStringExtra("timedate");
        String day = getIntent().getStringExtra("day");
        String noseat = getIntent().getStringExtra("noseat");

        txt_datetime.setText(timedate);
        txt_day.setText(day);
        txt_noofseat.setText(noseat+" "+"Seats");
        txt_view_day.setText(day);
        txt_view_day_2.setText(day);*/



        txtrestroname.setText(restoData.getRest_name());
        txt_time.setText(restoData.getEndtime());
        txt_totalkm.setText(restoData.getDistance()+" km");
        booking_id = restoData.getId();



        img_searchicon.setOnClickListener(v -> {
            String str_search=etv_searchview.getText().toString();
            PickupSelect_Food_Fragment all_Food_fragment = PickupSelect_Food_Fragment.GetInstance();

            if (Utils.isNetworkAvailable(ctx)) {
                all_Food_fragment.callApi_searchfooditem(category_id,str_search);
            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
            }

        });

        etv_searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(s.toString().length()==0)
                {
                    Hidekeyboard();
                    if (Utils.isNetworkAvailable(ctx)) {
                        PickupSelect_Food_Fragment all_Food_fragment = PickupSelect_Food_Fragment.GetInstance();
                        all_Food_fragment.callApi_fooditem(category_id);
                    }
                    else{
                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }


        });

        btn_view_cart.setOnClickListener(v -> {
            //showAlertView();
            PickupSelect_Food_Fragment all_Food_fragment = PickupSelect_Food_Fragment.GetInstance();
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
                tabLayout.selectTab(tabLayout.getTabAt(position));
                current_tabactive=position;

                FoodList_Tab foodList_tab = foodListArrayList.get(position);
                category_id = foodList_tab.getId();

                PickupSelect_Food_Fragment all_Food_fragment = PickupSelect_Food_Fragment.GetInstance();
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
                //tab.setText(foodList_tab.getName().toLowerCase());
                tab.setCustomView(getTabView(foodList_tab.getName().toLowerCase()));
                //TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                //tabOne.setText(foodList_tab.getName());
                //tabLayout.getTabAt(i).setCustomView(tabOne);
                //tabOne.setText(foodList_tab.getName().trim().toLowerCase());
                //tab.setCustomView(tabOne);
                //tabLayout.addTab(tabLayout.newTab().setText(foodList_tab.getName().trim().toLowerCase()));

            }
        }).attach();
    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickupSelectFood_Activity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablefood_reservealert, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_share_order;
        btn_share_order=dialogView.findViewById(R.id.btn_share_order);

        btn_share_order.setOnClickListener(v -> {

            Intent intent = new Intent(PickupSelectFood_Activity.this, BookingOrder_ReserverConformationActivity.class);
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
                                        ViewPagerAdapter_PickupSelectFood viewPagerAdapter_reserveSeat=new ViewPagerAdapter_PickupSelectFood(getSupportFragmentManager(),getLifecycle(),foodListArrayList, tableList, restoData);
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

    private void Hidekeyboard()
    {
        etv_searchview.clearFocus();
        InputMethodManager in = (InputMethodManager) PickupSelectFood_Activity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(etv_searchview.getWindowToken(), 0);
    }


    public View getTabView(String str) {
        View tab = LayoutInflater.from(PickupSelectFood_Activity.this).inflate(R.layout.custom_tab, null);
        TextView tv = tab.findViewById(R.id.custom_text);
        tv.setText(str);
        return tab;
    }
}