package com.forkmang.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.forkmang.activity.PickupSelectFood_Activity;
import com.forkmang.data.RestoData;

import java.util.ArrayList;


public class Pickup_Fragment_BookTableAdapter extends RecyclerView.Adapter<Pickup_Fragment_BookTableAdapter.BookTableItemHolder> {
    Activity activity;
    ArrayList<RestoData> resto_dataArrayList;

    public Pickup_Fragment_BookTableAdapter(Activity activity, ArrayList<RestoData> resto_dataArrayList, String coming_from, Context ctx) {
        this.activity = activity;
        this.resto_dataArrayList = resto_dataArrayList;
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
        RestoData bookTable = resto_dataArrayList.get(position);
        holder.txtrestroname.setText(bookTable.getRest_name());
        holder.txt_endtime.setText(bookTable.getEndtime());
        holder.txttotalkm.setText(bookTable.getDistance()+" Km");
    }

    @Override
    public int getItemCount(){
        return resto_dataArrayList.size();
    }

    public class BookTableItemHolder extends RecyclerView.ViewHolder {

        ImageView imgproduct;
        TextView txtrestroname, txt_endtime, txt_distance,txttotalkm,txt_ratingno;
        RatingBar rating_bar;
        RelativeLayout relative_view;

        public BookTableItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view = itemView.findViewById(R.id.relative_view);
            txtrestroname=itemView.findViewById(R.id.txtrestroname);
            txt_endtime=itemView.findViewById(R.id.txt_endtime);
            txt_distance=itemView.findViewById(R.id.txt_distance);
            txttotalkm=itemView.findViewById(R.id.txt_totalkm);
            txt_ratingno=itemView.findViewById(R.id.txt_ratingno);
            imgproduct=itemView.findViewById(R.id.imgrestro);
            rating_bar=itemView.findViewById(R.id.rating_bar);

            relative_view.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                RestoData restoData = resto_dataArrayList.get(position);
                final Intent mainIntent = new Intent(activity, PickupSelectFood_Activity.class);
                mainIntent.putExtra("restromodel", restoData);
                activity.startActivity(mainIntent);

                //activity.finish();
            });

        }
    }

}
