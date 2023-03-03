package com.forkmang.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.Walkin_listing_Adapter.RestoListItemHolder
import com.forkmang.data.RestoData
import com.forkmang.fragment.Walkin_detail_Fragment

class Walkin_listing_Adapter(
    var coming_from: String?,
    private val ItemOnClick: ((restId: String, restroData: RestoData?) -> Unit)
) : ListAdapter<RestoData, Walkin_listing_Adapter.RestoListItemHolder>(ItemCallback) {

    object ItemCallback : DiffUtil.ItemCallback<RestoData>() {
        override fun areItemsTheSame(oldItem: RestoData, newItem: RestoData): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: RestoData, newItem: RestoData): Boolean =
            oldItem == newItem
    }

    var resto_dataArrayList: List<RestoData> = emptyList()
        set(value) {
            field = value
            submitList(field)
        }

    /*book_table_cell*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestoListItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.book_table_cell, null)
        return RestoListItemHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestoListItemHolder, position: Int) {
        val bookTable = resto_dataArrayList[position]
        holder.txtrestroname.text = bookTable.rest_name
        holder.txt_endtime.text = bookTable.endtime
        holder.txttotalkm.text = bookTable.distance + " Km"
    }


    inner class RestoListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgproduct: ImageView
        var txtrestroname: TextView
        var txt_endtime: TextView
        var txt_distance: TextView
        var txttotalkm: TextView
        var txt_ratingno: TextView
        var rating_bar: RatingBar
        var relative_view: RelativeLayout

        init {
            relative_view = itemView.findViewById(R.id.relative_view)
            txtrestroname = itemView.findViewById(R.id.txtrestroname)
            txt_endtime = itemView.findViewById(R.id.txt_endtime)
            txt_distance = itemView.findViewById(R.id.txt_distance)
            txttotalkm = itemView.findViewById(R.id.txt_totalkm)
            txt_ratingno = itemView.findViewById(R.id.txt_ratingno)
            imgproduct = itemView.findViewById(R.id.imgrestro)
            rating_bar = itemView.findViewById(R.id.rating_bar)
            relative_view.setOnClickListener {
                val position = bindingAdapterPosition
                val restoData = resto_dataArrayList[position]
                val resturant_id = restoData.id
                if (coming_from.equals("listing", ignoreCase = true)) {
                    ItemOnClick(resturant_id?:"", restoData)
                } else {
                    ItemOnClick(resturant_id?:"",null)
                }
            }
        }
    }
}