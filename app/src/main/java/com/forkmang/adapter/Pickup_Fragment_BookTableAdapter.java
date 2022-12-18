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
import com.forkmang.activity.Activity_PaymentSummary_PickupFragment;
import com.forkmang.activity.Activity_PaymentSummary_WalkinFragment;
import com.forkmang.activity.BookingTableScreen2;
import com.forkmang.activity.WalkinInQuee;
import com.forkmang.data.BookTable_Data;

import java.util.ArrayList;


public class Pickup_Fragment_BookTableAdapter extends RecyclerView.Adapter<Pickup_Fragment_BookTableAdapter.BookTableItemHolder> {
    Activity activity;
    ArrayList<BookTable_Data> bookTable_dataArrayList;

    public Pickup_Fragment_BookTableAdapter(Activity activity, ArrayList<BookTable_Data> bookTable_dataArrayList) {
        this.activity = activity;
        this.bookTable_dataArrayList = bookTable_dataArrayList;
    }

    public Pickup_Fragment_BookTableAdapter(Activity activity) {
        this.activity = activity;
    }

    /*book_table_cell*/

    @NonNull
    @Override
    public BookTableItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_table_cell, null);
        BookTableItemHolder bookTableItemHolder = new BookTableItemHolder(v);
        return bookTableItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookTableItemHolder holder, int position) {
    }

    @Override
    public int getItemCount(){
       //return bookTable_dataArrayList.size();
        return 10;
    }

    public class BookTableItemHolder extends RecyclerView.ViewHolder {

        ImageView imgproduct;
        TextView txtproductname, txt_time, txt_distance,txttotalkm,txt_ratingno;
        RatingBar rating_bar;
        RelativeLayout relative_view;
        public BookTableItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view = itemView.findViewById(R.id.relative_view);
            txtproductname=itemView.findViewById(R.id.txtproductname);
            txt_time=itemView.findViewById(R.id.txt_time);
            txt_distance=itemView.findViewById(R.id.txt_distance);
            txttotalkm=itemView.findViewById(R.id.txttotalkm);
            txt_ratingno=itemView.findViewById(R.id.txt_ratingno);
            imgproduct=itemView.findViewById(R.id.imgproduct);
            rating_bar=itemView.findViewById(R.id.rating_bar);

            relative_view.setOnClickListener(v -> {
                int position = getAdapterPosition();

                final Intent mainIntent = new Intent(activity, Activity_PaymentSummary_PickupFragment.class);
                activity.startActivity(mainIntent);

                //activity.finish();
            });

        }
    }

}
