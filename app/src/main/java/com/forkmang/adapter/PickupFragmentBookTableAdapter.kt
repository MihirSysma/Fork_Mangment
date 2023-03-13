package com.forkmang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.RestoData

class PickupFragmentBookTableAdapter(
    var onItemClicked: ((clickPosition: RestoData) -> Unit)
) : ListAdapter<RestoData, PickupFragmentBookTableAdapter.BookTableItemHolder>(ItemCallback) {

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookTableItemHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.book_table_cell, null)
        return BookTableItemHolder(v)
    }


    override fun onBindViewHolder(holder: BookTableItemHolder, position: Int) {
        val bookTable: RestoData = resto_dataArrayList[position]
        holder.txtrestroname.text = bookTable.rest_name
        holder.txtEndtime.text = bookTable.endtime
        holder.txttotalkm.text = bookTable.distance + " Km"
    }


    inner class BookTableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgproduct: ImageView
        var txtrestroname: TextView
        var txtEndtime: TextView
        var txtDistance: TextView
        var txttotalkm: TextView
        var txtRatingno: TextView
        var ratingBar: RatingBar
        var relativeView: RelativeLayout

        init {
            relativeView = itemView.findViewById<RelativeLayout>(R.id.relative_view)
            txtrestroname = itemView.findViewById<TextView>(R.id.txtrestroname)
            txtEndtime = itemView.findViewById<TextView>(R.id.txt_endtime)
            txtDistance = itemView.findViewById<TextView>(R.id.txt_distance)
            txttotalkm = itemView.findViewById<TextView>(R.id.txt_totalkm)
            txtRatingno = itemView.findViewById<TextView>(R.id.txt_ratingno)
            imgproduct = itemView.findViewById<ImageView>(R.id.imgrestro)
            ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar)
            relativeView.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val restoData: RestoData = resto_dataArrayList[position]
                onItemClicked(restoData)
            }
        }
    }
}