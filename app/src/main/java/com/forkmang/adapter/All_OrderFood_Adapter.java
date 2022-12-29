package com.forkmang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.data.OrderFood_All;
import com.forkmang.fragment.All_OrderFood_Fragment;

import java.util.ArrayList;


public class All_OrderFood_Adapter extends RecyclerView.Adapter<All_OrderFood_Adapter.AllOrderFoodItemHolder> {
    Activity activity;
    ArrayList<OrderFood_All> orderFood_alls;
    All_OrderFood_Fragment all_orderFood_fragment;
    int row_index;

    public All_OrderFood_Adapter(Activity activity, ArrayList<OrderFood_All> orderFood_alls,All_OrderFood_Fragment all_orderFood_fragment) {
        this.activity = activity;
        this.orderFood_alls = orderFood_alls;
        this.all_orderFood_fragment = all_orderFood_fragment;
    }

    public All_OrderFood_Adapter(Activity activity, All_OrderFood_Fragment all_orderFood_fragment) {
        this.activity = activity;
        this.all_orderFood_fragment = all_orderFood_fragment;

    }

    /*book_table_cell*/

    @NonNull
    @Override
    public AllOrderFoodItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_food_order, null);
        AllOrderFoodItemHolder allOrderFoodItemHolder = new AllOrderFoodItemHolder(v);
        return allOrderFoodItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderFoodItemHolder holder, int position) {


    }

    @Override
    public int getItemCount(){
       //return orderFood_alls.size();
        return 5;
    }

    public class AllOrderFoodItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_view;
        public AllOrderFoodItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view =  itemView.findViewById(R.id.relative_view);
            relative_view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                row_index = position;

               /* relative_view.setOnClickListener(view -> {
                    row_index=position;

                });
                    if(row_index==position){
                        relative_view.setBackgroundColor(Color.parseColor("#c5c5c5"));
                    }
                    else
                    {
                        relative_view.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    }
               */

                all_orderFood_fragment.showAlertView();

            });

        }
    }



}
