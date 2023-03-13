package com.forkmang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.RestoData

class WalkinListingAdapter(
    var coming_from: String?,
    private val ItemOnClick: ((restId: String, restroData: RestoData?) -> Unit)
) : ListAdapter<RestoData, WalkinListingAdapter.RestoListItemHolder>(ItemCallback) {

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
        holder.txtEndTime.text = bookTable.endtime
        holder.txttotalkm.text = bookTable.distance + " Km"
    }


    inner class RestoListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgproduct: ImageView
        var txtrestroname: TextView
        var txtEndTime: TextView
        var txtDistance: TextView
        var txttotalkm: TextView
        var txtRatingNo: TextView
        var ratingBar: RatingBar
        var relativeView: RelativeLayout

        init {
            relativeView = itemView.findViewById(R.id.relative_view)
            txtrestroname = itemView.findViewById(R.id.txtrestroname)
            txtEndTime = itemView.findViewById(R.id.txt_endtime)
            txtDistance = itemView.findViewById(R.id.txt_distance)
            txttotalkm = itemView.findViewById(R.id.txt_totalkm)
            txtRatingNo = itemView.findViewById(R.id.txt_ratingno)
            imgproduct = itemView.findViewById(R.id.imgrestro)
            ratingBar = itemView.findViewById(R.id.rating_bar)
            relativeView.setOnClickListener {
                val position = bindingAdapterPosition
                val restoData = resto_dataArrayList[position]
                val resturantId = restoData.id
                if (coming_from.equals("listing", ignoreCase = true)) {
                    ItemOnClick(resturantId ?: "", restoData)
                } else {
                    ItemOnClick(resturantId ?: "", null)
                }
            }
        }
    }
}