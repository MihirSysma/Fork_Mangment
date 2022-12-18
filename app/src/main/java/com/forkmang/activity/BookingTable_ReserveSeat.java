package com.forkmang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter_ReserveSeat;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookingTable_ReserveSeat extends AppCompatActivity {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Button btn_view_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingtable_reserveseat);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        btn_view_cart = findViewById(R.id.btn_view_cart);

        btn_view_cart.setOnClickListener(v -> {
            showAlertView();
        });

        ViewPagerAdapter_ReserveSeat viewPagerAdapter_reserveSeat=new ViewPagerAdapter_ReserveSeat(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(viewPagerAdapter_reserveSeat);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0){
                tab.setText("All");
            }
            else if(position==1){
                tab.setText("Pizza");
            }
            else if(position==2){
                tab.setText("Combo");
            }
            else if(position==3){
                tab.setText("Pasta");
            }
            else if(position==4){
                tab.setText("Desert");
            }
        }).attach();



    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTable_ReserveSeat.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablefood_reservealert, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_share_order;
        btn_share_order=dialogView.findViewById(R.id.btn_share_order);

        btn_share_order.setOnClickListener(v -> {

            Intent intent = new Intent(BookingTable_ReserveSeat.this, BookingOrder_ReserverConformationActivity.class);
            startActivity(intent);
            dialog.dismiss();

         });
        dialog.show();

    }
}