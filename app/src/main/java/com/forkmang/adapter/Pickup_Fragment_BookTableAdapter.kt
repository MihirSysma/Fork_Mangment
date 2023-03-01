package com.forkmang.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.activity.PickupSelectFood_Activity
import com.forkmang.data.RestoData

class Pickup_Fragment_BookTableAdapter :
    RecyclerView.Adapter<Pickup_Fragment_BookTableAdapter.BookTableItemHolder?> {
    var activity: Activity
    var resto_dataArrayList: ArrayList<RestoData>? = null
    lateinit var onItemClicked:((clickPosition: RestoData) -> Unit)

    constructor(
        activity: Activity,
        resto_dataArrayList: ArrayList<RestoData>?,
        coming_from: String?,
        ctx: Context?,
        onItemClicked: ((clickPosition: RestoData) -> Unit)
    ) {
        this.activity = activity
        this.resto_dataArrayList = resto_dataArrayList
        this.onItemClicked = onItemClicked
    }

    constructor(activity: Activity) {
        this.activity = activity
    }

    /*book_table_cell*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookTableItemHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.book_table_cell, null)
        return BookTableItemHolder(v)
    }

    override fun getItemCount() = resto_dataArrayList!!.size

    override fun onBindViewHolder(holder: BookTableItemHolder, position: Int) {
        val bookTable: RestoData = resto_dataArrayList!![position]
        holder.txtrestroname.text = bookTable.rest_name
        holder.txt_endtime.text = bookTable.endtime
        holder.txttotalkm.text = bookTable.distance + " Km"
    }


    inner class BookTableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgproduct: ImageView
        var txtrestroname: TextView
        var txt_endtime: TextView
        var txt_distance: TextView
        var txttotalkm: TextView
        var txt_ratingno: TextView
        var rating_bar: RatingBar
        var relative_view: RelativeLayout

        init {
            relative_view = itemView.findViewById<RelativeLayout>(R.id.relative_view)
            txtrestroname = itemView.findViewById<TextView>(R.id.txtrestroname)
            txt_endtime = itemView.findViewById<TextView>(R.id.txt_endtime)
            txt_distance = itemView.findViewById<TextView>(R.id.txt_distance)
            txttotalkm = itemView.findViewById<TextView>(R.id.txt_totalkm)
            txt_ratingno = itemView.findViewById<TextView>(R.id.txt_ratingno)
            imgproduct = itemView.findViewById<ImageView>(R.id.imgrestro)
            rating_bar = itemView.findViewById<RatingBar>(R.id.rating_bar)
            relative_view.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val restoData: RestoData? = resto_dataArrayList?.get(position)
                if (restoData != null) {
                    onItemClicked(restoData)
                }
            }
        }
    }
}