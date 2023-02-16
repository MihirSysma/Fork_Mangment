package com.forkmang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.data.Category_ItemList;
import com.forkmang.data.RestoData;
import com.forkmang.fragment.PickupSelect_Food_Fragment;

import java.util.ArrayList;


public class PickupFoodList_Adapter extends RecyclerView.Adapter<PickupFoodList_Adapter.AllOrderFoodItemHolder> {
    Activity activity;
    Context ctx;
    PickupSelect_Food_Fragment pickupSelect_food_fragment;
    ArrayList<Category_ItemList> category_itemLists;
    RestoData restoData;
    int row_index;

    public PickupFoodList_Adapter(Context ctx, Activity activity, ArrayList<Category_ItemList> category_itemLists , PickupSelect_Food_Fragment pickupSelect_food_fragment, RestoData restoData) {
        this.ctx = ctx;
        this.activity = activity;
        this.category_itemLists = category_itemLists;
        this.pickupSelect_food_fragment = pickupSelect_food_fragment;
        this.restoData = restoData;
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
        Category_ItemList category_itemList = category_itemLists.get(position);
        holder.txt_foodname.setText(category_itemList.getName());
        holder.txt_price.setText(ctx.getResources().getString(R.string.rupee)+category_itemList.getPrice());

    }

    @Override
    public int getItemCount(){
       return category_itemLists.size();

    }

    public class AllOrderFoodItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_view;
        ImageView img_food;
        TextView txt_foodname, txt_price;
        public AllOrderFoodItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view =  itemView.findViewById(R.id.relative_view);
            img_food =  itemView.findViewById(R.id.img_food);
            txt_foodname =  itemView.findViewById(R.id.txt_foodname);
            txt_price =  itemView.findViewById(R.id.txt_price);

            relative_view.setOnClickListener(v -> {


                int position = getBindingAdapterPosition();
                Category_ItemList category_itemList = category_itemLists.get(position);
                row_index = position;
                pickupSelect_food_fragment.showAlertView(category_itemList);
                //final Intent mainIntent = new Intent(ctx, PickupSelectFood_Activity.class);
                //mainIntent.putExtra("restromodel", restoData);
                //ctx.startActivity(mainIntent);

            });

        }
    }



}
