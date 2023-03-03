package com.forkmang.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.OrderFood_All

class CartViewAdapter(var activity: Activity, var orderFood_alls: ArrayList<OrderFood_All>?) :
    RecyclerView.Adapter<CartViewAdapter.AllOrderFoodItemHolder>() {
    var row_index = 0

    /*book_table_cell*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrderFoodItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_food_order, null)
        return AllOrderFoodItemHolder(v)
    }

    override fun onBindViewHolder(holder: AllOrderFoodItemHolder, position: Int) {}
    override fun getItemCount(): Int {
        //return orderFood_alls.size();
        return 5
    }

    inner class AllOrderFoodItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var relative_view: RelativeLayout

        init {
            relative_view = itemView.findViewById(R.id.relative_view)
        }
    }
}