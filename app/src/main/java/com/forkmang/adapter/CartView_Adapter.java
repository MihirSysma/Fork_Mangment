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


public class CartView_Adapter extends RecyclerView.Adapter<CartView_Adapter.AllOrderFoodItemHolder> {
    Activity activity;
    ArrayList<OrderFood_All> orderFood_alls;
    int row_index;

    public CartView_Adapter(Activity activity, ArrayList<OrderFood_All> orderFood_alls) {
        this.activity = activity;
        this.orderFood_alls = orderFood_alls;
    }

    public CartView_Adapter(Activity activity) {
        this.activity = activity;
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

        }
    }



}
