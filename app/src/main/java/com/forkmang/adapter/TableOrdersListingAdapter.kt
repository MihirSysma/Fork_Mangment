package com.forkmang.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.TableOrdersListingAdapter.BookTable_Orders_ItemHolder
import com.forkmang.data.TableOrderListing

class TableOrdersListingAdapter(
    var activity: Activity,
    var bookTable_dataArrayList: ArrayList<TableOrderListing>?
) : RecyclerView.Adapter<BookTable_Orders_ItemHolder>() {

    /*book_table_cell for orders*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookTable_Orders_ItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_cell, null)
        return BookTable_Orders_ItemHolder(v)
    }

    override fun onBindViewHolder(holder: BookTable_Orders_ItemHolder, position: Int) {
        val tableOrderListing = bookTable_dataArrayList!![position]
        holder.txt_order_id.text = "Order Id: " + tableOrderListing.order_id
        holder.txt_restroname.text = tableOrderListing.str_restroname
        holder.txt_restrotime.text = "Open Till: " + tableOrderListing.resturant_timing + " AM/PM"
        holder.txt_restrocontact.text = "Contact No: " + tableOrderListing.resturant_contact
        holder.txt_restrobranch.text = "Branch: " + tableOrderListing.resturant_branch
        holder.txt_payment_status.text = "Payment Status: " + tableOrderListing.payment_status
        holder.txt_order_status.text = "Order Status: " + tableOrderListing.order_status


        /* if(position == 0)
        {
            holder.linear_view_layout_1.setVisibility(View.VISIBLE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.GONE);

        }
        else if(position == 1)
        {
            holder.linear_view_layout_2.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }
        else if(position == 2)
        {
            holder.linear_view_layout_3.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }
        else if(position == 3)
        {
            holder.linear_view_layout_1.setVisibility(View.VISIBLE);
            holder.linear_view_layout_2.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.GONE);
        }
        else if(position == 4)
        {
            holder.linear_view_layout_2.setVisibility(View.VISIBLE);
            holder.linear_view_layout_1.setVisibility(View.GONE);
            holder.linear_view_layout_3.setVisibility(View.GONE);
            holder.linear_starrating.setVisibility(View.VISIBLE);
        }*/
    }

    override fun getItemCount() = bookTable_dataArrayList?.size ?: 0

    inner class BookTable_Orders_ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linear_view_layout_1: LinearLayout
        var linear_view_layout_2: LinearLayout
        var linear_view_layout_3: LinearLayout
        var linear_starrating: LinearLayout
        var edit_rating_img: ImageView
        var txt_order_id: TextView
        var txt_restroname: TextView
        var txt_restrotime: TextView
        var txt_restrocontact: TextView
        var txt_restrobranch: TextView
        var txt_payment_status: TextView
        var txt_order_status: TextView

        init {
            linear_view_layout_1 = itemView.findViewById(R.id.linear_view_layout_1)
            linear_view_layout_2 = itemView.findViewById(R.id.linear_view_layout_2)
            linear_view_layout_3 = itemView.findViewById(R.id.linear_view_layout_3)
            linear_starrating = itemView.findViewById(R.id.linear_starrating)
            edit_rating_img = itemView.findViewById(R.id.edit_rating_img)
            txt_order_id = itemView.findViewById(R.id.txt_queueno)
            txt_restroname = itemView.findViewById(R.id.txt_restroname)
            txt_restrotime = itemView.findViewById(R.id.txt_restrotime)
            txt_restrocontact = itemView.findViewById(R.id.txt_restrocontact)
            txt_restrobranch = itemView.findViewById(R.id.txt_restrobranch)
            txt_payment_status = itemView.findViewById(R.id.txt_payment_status)
            txt_order_status = itemView.findViewById(R.id.txt_order_status)
            edit_rating_img.setOnClickListener { showAlertViewRatingView() }
        }
    }

    private fun showAlertViewRatingView() {
        val alertDialog = AlertDialog.Builder(activity)
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.rating_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val Btn_Submit: Button = dialogView.findViewById(R.id.btn_submit)
        Btn_Submit.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}