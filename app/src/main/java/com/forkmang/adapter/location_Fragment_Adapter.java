package com.forkmang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.Activity_PaymentSummary;
import com.forkmang.data.Location;

import java.util.ArrayList;


public class location_Fragment_Adapter extends RecyclerView.Adapter<location_Fragment_Adapter.LocationItemHolder> {
    Activity activity;
    ArrayList<Location> locationArrayList;

    public location_Fragment_Adapter(Activity activity, ArrayList<Location> locationArrayList) {
        this.activity = activity;
        this.locationArrayList = locationArrayList;
    }

    public location_Fragment_Adapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public LocationItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_cell, null);
        LocationItemHolder locationItemHolder = new LocationItemHolder(v);
        return locationItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationItemHolder holder, int position) {
        if(position%2 == 0)
        {
            holder.imgrightchek.setVisibility(View.VISIBLE);

        }
        else{
            holder.imgrightchek.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
       //return bookTable_dataArrayList.size();
        return 10;
    }

    public class LocationItemHolder extends RecyclerView.ViewHolder {
        ImageView imgrightchek;
        LinearLayout linear_layout_default;
        public LocationItemHolder(@NonNull View itemView) {
            super(itemView);
            imgrightchek = itemView.findViewById(R.id.imgrightchek);
            linear_layout_default = itemView.findViewById(R.id.linear_layout_default);

            linear_layout_default.setOnClickListener(v -> {
                showAlertView_2();
                //showAlertView();
            });

        }
    }

    public void showAlertView_2()
    {
        final Dialog dialog = new Dialog(activity,R.style.FullHeightDialog);
        dialog.setContentView(R.layout.location_alertview);

        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Button btn_save_location;
        ImageView img_close;
        btn_save_location = dialog.findViewById(R.id.btn_save_location);
        img_close = dialog.findViewById(R.id.img_close);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_save_location.setOnClickListener(v -> {

        });

        dialog.show();

    }


    public  void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.location_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_save_location ;
        btn_save_location=dialogView.findViewById(R.id.btn_save_location);
        dialog.show();

    }
}
