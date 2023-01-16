package com.forkmang.adapter;

import android.annotation.SuppressLint;
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
import com.forkmang.data.RestoData;
import com.forkmang.fragment.Walkin_detail_Fragment;

import java.util.ArrayList;


public class Walkin_listing_Adapter extends RecyclerView.Adapter<Walkin_listing_Adapter.RestoListItemHolder> {
    Activity activity;
    ArrayList<RestoData> resto_dataArrayList;
    String coming_from;
    Context ctx;

    public Walkin_listing_Adapter(Activity activity, ArrayList<RestoData> bookTable_dataArrayList,String coming_from, Context ctx) {
        this.activity = activity;
        this.resto_dataArrayList = bookTable_dataArrayList;
        this.coming_from = coming_from;
        this.ctx=ctx;
    }

    public Walkin_listing_Adapter(Activity activity) {
        this.activity = activity;
    }

    /*book_table_cell*/
    @NonNull
    @Override
    public RestoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_table_cell, null);
        RestoListItemHolder bookTableItemHolder = new RestoListItemHolder(v);
        return bookTableItemHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RestoListItemHolder holder, int position) {
        RestoData bookTable = resto_dataArrayList.get(position);
        holder.txtrestroname.setText(bookTable.getRest_name());
        holder.txt_endtime.setText(bookTable.getEndtime());
        holder.txttotalkm.setText(bookTable.getDistance()+" Km");
    }

    @Override
    public int getItemCount(){
       return resto_dataArrayList.size();
    }

    public class RestoListItemHolder extends RecyclerView.ViewHolder {
        ImageView imgproduct;
        TextView txtrestroname, txt_endtime, txt_distance,txttotalkm,txt_ratingno;
        RatingBar rating_bar;
        RelativeLayout relative_view;
        public RestoListItemHolder(@NonNull View itemView) {
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
                String resturant_id = restoData.getId();

                if(coming_from.equalsIgnoreCase("listing"))
                {
                    Intent intent = new Intent(activity, Walkin_detail_Fragment.class);
                    intent.putExtra("resturant_id", resturant_id);
                    intent.putExtra("restromodel", restoData);
                    activity.startActivity(intent);
                }
                else{
                      ((Walkin_detail_Fragment) ctx).callapi_getquess(resturant_id);
                }
                //activity.finish();
            });

        }
    }



}
