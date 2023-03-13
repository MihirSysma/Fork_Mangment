package com.forkmang.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.Category_ItemList

class FoodListAdapter(
    var ctx: Context,
    var activity: Activity,
    var category_itemLists: ArrayList<Category_ItemList>,
    private val onItemClicked: ((categoryItemList: Category_ItemList) -> Unit)
) : RecyclerView.Adapter<FoodListAdapter.AllOrderFoodItemHolder>() {
    var rowIndex = 0

    /*book_table_cell*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrderFoodItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_food_order, null)
        return AllOrderFoodItemHolder(v)
    }

    override fun onBindViewHolder(holder: AllOrderFoodItemHolder, position: Int) {
        val categoryItemList = category_itemLists[position]
        holder.txtFoodName.text = categoryItemList.name
        holder.txtPrice.text = ctx.resources.getString(R.string.rupee) + categoryItemList.price
    }

    override fun getItemCount(): Int {
        return category_itemLists.size
    }

    inner class AllOrderFoodItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var relativeView: RelativeLayout
        var imgFood: ImageView
        var txtFoodName: TextView
        var txtPrice: TextView

        init {
            relativeView = itemView.findViewById(R.id.relative_view)
            imgFood = itemView.findViewById(R.id.img_food)
            txtFoodName = itemView.findViewById(R.id.txt_foodname)
            txtPrice = itemView.findViewById(R.id.txt_price)
            relativeView.setOnClickListener { v: View? ->
                val position = bindingAdapterPosition
                val categoryItemlist = category_itemLists[position]
                rowIndex = position
                onItemClicked(categoryItemlist)
            }
        }
    }
}