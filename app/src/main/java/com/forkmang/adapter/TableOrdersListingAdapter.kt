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
import com.forkmang.adapter.TableOrdersListingAdapter.BookTableOrdersItemHolder
import com.forkmang.data.TableOrderListing

class TableOrdersListingAdapter(
    var activity: Activity,
    var bookTable_dataArrayList: ArrayList<TableOrderListing>?
) : RecyclerView.Adapter<BookTableOrdersItemHolder>() {

    /*book_table_cell for orders*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookTableOrdersItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_cell, null)
        return BookTableOrdersItemHolder(v)
    }

    override fun onBindViewHolder(holder: BookTableOrdersItemHolder, position: Int) {
        val tableOrderListing = bookTable_dataArrayList!![position]
        holder.txtOrderId.text = "Order Id: " + tableOrderListing.order_id
        holder.txtRestroName.text = tableOrderListing.str_restroname
        holder.txtRestroTime.text = "Open Till: " + tableOrderListing.resturant_timing + " AM/PM"
        holder.txtRestroContact.text = "Contact No: " + tableOrderListing.resturant_contact
        holder.txtRestroBranch.text = "Branch: " + tableOrderListing.resturant_branch
        holder.txtPaymentStatus.text = "Payment Status: " + tableOrderListing.payment_status
        holder.txtOrderStatus.text = "Order Status: " + tableOrderListing.order_status


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

    inner class BookTableOrdersItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearViewLayout1: LinearLayout
        var linearViewLayout2: LinearLayout
        var linearViewLayout3: LinearLayout
        var linearStarRating: LinearLayout
        var editRatingImg: ImageView
        var txtOrderId: TextView
        var txtRestroName: TextView
        var txtRestroTime: TextView
        var txtRestroContact: TextView
        var txtRestroBranch: TextView
        var txtPaymentStatus: TextView
        var txtOrderStatus: TextView

        init {
            linearViewLayout1 = itemView.findViewById(R.id.linear_view_layout_1)
            linearViewLayout2 = itemView.findViewById(R.id.linear_view_layout_2)
            linearViewLayout3 = itemView.findViewById(R.id.linear_view_layout_3)
            linearStarRating = itemView.findViewById(R.id.linear_starrating)
            editRatingImg = itemView.findViewById(R.id.edit_rating_img)
            txtOrderId = itemView.findViewById(R.id.txt_queueno)
            txtRestroName = itemView.findViewById(R.id.txt_restroname)
            txtRestroTime = itemView.findViewById(R.id.txt_restrotime)
            txtRestroContact = itemView.findViewById(R.id.txt_restrocontact)
            txtRestroBranch = itemView.findViewById(R.id.txt_restrobranch)
            txtPaymentStatus = itemView.findViewById(R.id.txt_payment_status)
            txtOrderStatus = itemView.findViewById(R.id.txt_order_status)
            editRatingImg.setOnClickListener { showAlertViewRatingView() }
        }
    }

    private fun showAlertViewRatingView() {
        val alertDialog = AlertDialog.Builder(activity)
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.rating_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val btnSubmit: Button = dialogView.findViewById(R.id.btn_submit)
        btnSubmit.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}