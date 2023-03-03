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
import com.forkmang.fragment.SelectFoodFragment

class AllFoodAdapter2(
    var ctx: Context,
    var activity: Activity,
    var category_itemLists: ArrayList<Category_ItemList>,
    var all_Food_fragment: SelectFoodFragment
) : RecyclerView.Adapter<AllFoodAdapter2.AllOrderFoodItemHolder>() {
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

    override fun onBindViewHolder(holder: AllOrderFoodItemHolder, position: Int) {
        val category_itemList = category_itemLists[position]
        holder.txt_foodname.text = category_itemList.name
        holder.txt_price.text = ctx.resources.getString(R.string.rupee) + category_itemList.price
    }

    override fun getItemCount(): Int {
        return category_itemLists.size
    }

    inner class AllOrderFoodItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var relative_view: RelativeLayout
        var img_food: ImageView
        var txt_foodname: TextView
        var txt_price: TextView

        init {
            relative_view = itemView.findViewById(R.id.relative_view)
            img_food = itemView.findViewById(R.id.img_food)
            txt_foodname = itemView.findViewById(R.id.txt_foodname)
            txt_price = itemView.findViewById(R.id.txt_price)
            relative_view.setOnClickListener {
                val position = bindingAdapterPosition
                val category_itemList = category_itemLists[position]
                row_index = position
                all_Food_fragment.showAlertView(category_itemList)
            }
        }
    }
}