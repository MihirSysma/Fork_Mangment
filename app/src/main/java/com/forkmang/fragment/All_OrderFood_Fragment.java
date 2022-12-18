package com.forkmang.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.Activity_PaymentSummary;
import com.forkmang.adapter.All_OrderFood_Adapter;
import com.forkmang.adapter.CartBookingAdapter;

public class All_OrderFood_Fragment extends Fragment {

    RecyclerView recyclerView;

    public static All_OrderFood_Fragment newInstance() {
        return new All_OrderFood_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderfood_layout, container, false);
        recyclerView = view.findViewById(R.id.order_food_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        All_OrderFood_Adapter all_orderFood_adapter = new All_OrderFood_Adapter(getActivity(), All_OrderFood_Fragment.this);
        recyclerView.setAdapter(all_orderFood_adapter);


        return view;

    }

    public  void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.addqty_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_add,btn_reserve ;
        TextView plus_btn, txt3, minus1;
        btn_add=dialogView.findViewById(R.id.btn_add);
        btn_reserve=dialogView.findViewById(R.id.btn_reserve);
        plus_btn=dialogView.findViewById(R.id.plus_btn);
        minus1=dialogView.findViewById(R.id.minus1);
        txt3=dialogView.findViewById(R.id.txt3);

        plus_btn.setOnClickListener(v -> {
            int value = Integer.parseInt(txt3.getText().toString());
            ++value;
            txt3.setText(String.valueOf(value));
        });

        minus1.setOnClickListener(v -> {
            int value = Integer.parseInt(txt3.getText().toString());
            --value;
            txt3.setText(String.valueOf(value));
        });

        btn_add.setOnClickListener(v -> {
            dialog.dismiss();
            showAlertView_2();

        });

        btn_reserve.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }


    public void showAlertView_2()
    {
        final Dialog dialog = new Dialog(getActivity(),R.style.FullHeightDialog);
        dialog.setContentView(R.layout.cartview_alertview_2);

        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Button btn_pay_table_food,btn_pay_table;
        RecyclerView recycleView;
        ImageView img_close;

        recycleView = dialog.findViewById(R.id.recycleview);
        btn_pay_table_food=dialog.findViewById(R.id.btn_pay_table_food);
        btn_pay_table=dialog.findViewById(R.id.btn_pay_table);
        img_close=dialog.findViewById(R.id.img_close);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(getActivity());
        recycleView.setAdapter(cartBookingAdapter);


        btn_pay_table_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final Intent mainIntent = new Intent(getContext(), Activity_PaymentSummary.class);
                startActivity(mainIntent);

            }
        });

        btn_pay_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }




}
