package com.forkmang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.BookTableAdapter;
import com.forkmang.adapter.ListTableBookingAdapter;

public class BookingTableScreen2 extends Activity {

    RecyclerView recyclerView;
    RadioButton button_floor,button_list;
    FrameLayout frame_layout;
    LinearLayout linear_layout_under_frame,linear_listview;
    Button btn_payment_conform;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_table_2);

        button_floor = findViewById(R.id.button_floor);
        button_list = findViewById(R.id.button_list);
        frame_layout = findViewById(R.id.frame_layout);
        linear_layout_under_frame = findViewById(R.id.linear_layout);
        btn_payment_conform = findViewById(R.id.btn_payment_conform);
        linear_listview = findViewById(R.id.linear_listview);

        button_floor.setChecked(true);


        btn_payment_conform.setOnClickListener(v -> {
            showAlertView();
        });


        button_floor.setOnClickListener(v -> {
            frame_layout.setVisibility(View.VISIBLE);
            linear_layout_under_frame.setVisibility(View.VISIBLE);
            linear_listview.setVisibility(View.GONE);

        });


        button_list.setOnClickListener(v -> {
            linear_listview.setVisibility(View.VISIBLE);

            frame_layout.setVisibility(View.GONE);
            linear_layout_under_frame.setVisibility(View.GONE);

            recyclerView = findViewById(R.id.table_recycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(BookingTableScreen2.this, LinearLayoutManager.HORIZONTAL, false));
            ListTableBookingAdapter listTableBookingAdapter = new ListTableBookingAdapter(BookingTableScreen2.this);
            recyclerView.setAdapter(listTableBookingAdapter);

        });
    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTableScreen2.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.tableselection_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_select_nxt;

        btn_select_nxt=dialogView.findViewById(R.id.btn_select_nxt);

        btn_select_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAlertView_paymentconform();

            }
        });
        dialog.show();

    }


    private void showAlertView_paymentconform()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTableScreen2.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.payment_conform_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_cancel,btn_cnf_payment,btn_select_food;

        btn_select_food=dialogView.findViewById(R.id.btn_select_food);
        btn_cancel=dialogView.findViewById(R.id.btn_cancel);

        btn_cnf_payment=dialogView.findViewById(R.id.btn_cnf_payment);

        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        btn_cnf_payment.setOnClickListener(v ->{

                showAlertView_conformtable();
                dialog.dismiss();

        });

        btn_select_food.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(BookingTableScreen2.this, BookingTable_ReserveSeat.class);
            startActivity(mainIntent);
            dialog.dismiss();
        });

        dialog.show();

    }

    private void showAlertView_conformtable()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTableScreen2.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablereserve_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();



        dialog.show();

    }


}
