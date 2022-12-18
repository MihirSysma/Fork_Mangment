package com.forkmang.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.BookingTableScreen2;
import com.forkmang.data.ListTableBooking;

import java.util.ArrayList;


public class ListTableBookingAdapter extends RecyclerView.Adapter<ListTableBookingAdapter.ListBookTableItemHolder> {
    Activity activity;
    ArrayList<ListTableBooking> listTableBookings;

    public ListTableBookingAdapter(Activity activity, ArrayList<ListTableBooking> listTableBookings) {
        this.activity = activity;
        this.listTableBookings = listTableBookings;
    }

    public ListTableBookingAdapter(Activity activity) {
        this.activity = activity;

    }


    @NonNull
    @Override
    public ListBookTableItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_cell, null);
        ListBookTableItemHolder listBookTableItemHolder = new ListBookTableItemHolder(v);
        return listBookTableItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListBookTableItemHolder holder, int position) {
    }

    @Override
    public int getItemCount(){
       //return bookTable_dataArrayList.size();
        return 5;
    }

    public class ListBookTableItemHolder extends RecyclerView.ViewHolder {
        public ListBookTableItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
