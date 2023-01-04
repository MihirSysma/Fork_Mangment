package com.forkmang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.data.CartBooking;

import java.util.ArrayList;


public class CartBookingAdapter extends RecyclerView.Adapter<CartBookingAdapter.CartProductItemHolder> {
    Context ctx;
    ArrayList<CartBooking> cartBookingArrayList;
    Activity activity;

    public CartBookingAdapter(Context ctx, ArrayList<CartBooking> cartBookingArrayList) {
        this.ctx = ctx;
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
        CartBooking cartBooking = cartBookingArrayList.get(position);
        holder.txtproductname.setText(cartBooking.getCart_item_details_name());
        holder.txt_toopings.setText(cartBooking.getExtra_item_details_name());
        holder.txt_qty.setText(cartBooking.getCart_item_qty());
        holder.txt_price.setText(ctx.getResources().getString(R.string.rupee)+cartBooking.getCart_item_details_price());


    }

    @Override
    public int getItemCount(){
       return cartBookingArrayList.size();

    }

    public class CartProductItemHolder extends RecyclerView.ViewHolder {
        TextView txtproductname ,txt_toopings, txt_type, txt_qty, plus_btn, minus_btn,txt_price;
        ImageView img_del, img_edit;
        public CartProductItemHolder(@NonNull View itemView) {
            super(itemView);
            txtproductname = itemView.findViewById(R.id.txtproductname);
            txt_toopings = itemView.findViewById(R.id.txt_toopings);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_price = itemView.findViewById(R.id.txt_price);
            plus_btn = itemView.findViewById(R.id.plus_btn);
            minus_btn = itemView.findViewById(R.id.minus_btn);
            img_del = itemView.findViewById(R.id.img_del);
            img_edit = itemView.findViewById(R.id.img_edit);
        }
    }

}
