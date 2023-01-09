package com.forkmang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.data.TableOrderListing;

import java.util.ArrayList;


public class TableOrdersListing_Adapter extends RecyclerView.Adapter<TableOrdersListing_Adapter.BookTable_Orders_ItemHolder> {
    Activity activity;
    ArrayList<TableOrderListing> bookTable_dataArrayList;

    public TableOrdersListing_Adapter(Activity activity, ArrayList<TableOrderListing> bookTable_dataArrayList) {
        this.activity = activity;
        this.bookTable_dataArrayList = bookTable_dataArrayList;
    }

    public TableOrdersListing_Adapter(Activity activity) {
        this.activity = activity;

    }

    /*book_table_cell for orders*/
    @NonNull
    @Override
    public BookTable_Orders_ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cell, null);
        return new BookTable_Orders_ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookTable_Orders_ItemHolder holder, int position) {

        TableOrderListing tableOrderListing = bookTable_dataArrayList.get(position);
        holder.txt_order_id.setText("Order Id: "+tableOrderListing.getOrder_id());
        holder.txt_restroname.setText(tableOrderListing.getStr_restroname());
        holder.txt_restrotime.setText("Open Till: "+tableOrderListing.getResturant_timing()+" AM/PM");
        holder.txt_restrocontact.setText("Contact No: "+tableOrderListing.getResturant_contact());
        holder.txt_restrobranch.setText("Branch: "+tableOrderListing.getResturant_branch());
        holder.txt_payment_status.setText("Payment Status: "+tableOrderListing.getPayment_status());
        holder.txt_order_status.setText("Order Status: "+tableOrderListing.getOrder_status());


       /* if(position == 0)
        {
            holder.linear_view_layout_1.setVisibility(View.VISIBLE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.GONE);

        }
        else if(position == 1)
        {
            holder.linear_view_layout_2.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }
        else if(position == 2)
        {
            holder.linear_view_layout_3.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }
        else if(position == 3)
        {
            holder.linear_view_layout_1.setVisibility(View.VISIBLE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.GONE);
        }
        else if(position == 4)
        {
            holder.linear_view_layout_2.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount(){
       return bookTable_dataArrayList.size();

    }

    public class BookTable_Orders_ItemHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_view_layout_1, linear_view_layout_2,linear_view_layout_3,linear_starrating;
        ImageView edit_rating_img;
        TextView txt_order_id, txt_restroname, txt_restrotime,txt_restrocontact,txt_restrobranch,txt_payment_status,txt_order_status;
        public BookTable_Orders_ItemHolder(@NonNull View itemView) {
            super(itemView);
            linear_view_layout_1 = itemView.findViewById(R.id.linear_view_layout_1);
            linear_view_layout_2 = itemView.findViewById(R.id.linear_view_layout_2);
            linear_view_layout_3 = itemView.findViewById(R.id.linear_view_layout_3);
            linear_starrating = itemView.findViewById(R.id.linear_starrating);
            edit_rating_img = itemView.findViewById(R.id.edit_rating_img);
            txt_order_id = itemView.findViewById(R.id.txt_order_id);
            txt_restroname = itemView.findViewById(R.id.txt_restroname);
            txt_restrotime = itemView.findViewById(R.id.txt_restrotime);
            txt_restrocontact = itemView.findViewById(R.id.txt_restrocontact);
            txt_restrobranch = itemView.findViewById(R.id.txt_restrobranch);
            txt_payment_status = itemView.findViewById(R.id.txt_payment_status);
            txt_order_status = itemView.findViewById(R.id.txt_order_status);

            edit_rating_img.setOnClickListener(v -> {
                showAlertView_RatingView();
            });
        }
    }


    private void showAlertView_RatingView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.rating_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button Btn_Submit ;

        Btn_Submit=dialogView.findViewById(R.id.btn_submit);

        Btn_Submit.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }





}
