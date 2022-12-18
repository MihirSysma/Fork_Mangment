package com.forkmang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.data.CartBooking;

import java.util.ArrayList;


public class CartBookingAdapter extends RecyclerView.Adapter<CartBookingAdapter.CartProductItemHolder> {
    Activity activity;
    ArrayList<CartBooking> cartBookingArrayList;

    public CartBookingAdapter(Activity activity, ArrayList<CartBooking> cartBookingArrayList) {
        this.activity = activity;
        this.cartBookingArrayList = cartBookingArrayList;
    }

    public CartBookingAdapter(Activity activity) {
        this.activity = activity;
    }
    @NonNull
    @Override
    public CartProductItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view_cell, null);
        CartProductItemHolder cartProductItemHolder = new CartProductItemHolder(v);
        return cartProductItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductItemHolder holder, int position) {
    }

    @Override
    public int getItemCount(){
       //return bookTable_dataArrayList.size();
        return 3;
    }

    public class CartProductItemHolder extends RecyclerView.ViewHolder {
        public CartProductItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
