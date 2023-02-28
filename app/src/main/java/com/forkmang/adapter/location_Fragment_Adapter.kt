package com.forkmang.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.location_Fragment_Adapter.LocationItemHolder
import com.forkmang.data.Location

class location_Fragment_Adapter : RecyclerView.Adapter<LocationItemHolder> {
    var activity: Activity
    var locationArrayList: ArrayList<Location>? = null

    constructor(activity: Activity, locationArrayList: ArrayList<Location>?) {
        this.activity = activity
        this.locationArrayList = locationArrayList
    }

    constructor(activity: Activity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.location_cell, null)
        return LocationItemHolder(v)
    }

    override fun onBindViewHolder(holder: LocationItemHolder, position: Int) {
        if (position % 2 == 0) {
            holder.imgrightchek.visibility = View.VISIBLE
        } else {
            holder.imgrightchek.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        //return bookTable_dataArrayList.size();
        return 10
    }

    inner class LocationItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgrightchek: ImageView
        var linear_layout_default: LinearLayout

        init {
            imgrightchek = itemView.findViewById(R.id.imgrightchek)
            linear_layout_default = itemView.findViewById(R.id.linear_layout_default)
            linear_layout_default.setOnClickListener { v: View? -> showAlertView_2() }
        }
    }

    fun showAlertView_2() {
        val dialog = Dialog(activity, R.style.FullHeightDialog)
        dialog.setContentView(R.layout.location_alertview)
        if (dialog.window != null) {
            dialog.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        val btn_save_location: Button = dialog.findViewById(R.id.btn_save_location)
        val img_close: ImageView = dialog.findViewById(R.id.img_close)
        img_close.setOnClickListener { dialog.dismiss() }
        btn_save_location.setOnClickListener { }
        dialog.show()
    }

    fun showAlertView() {
        val alertDialog = AlertDialog.Builder(activity)
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.quee_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val btn_save_location: Button = dialogView.findViewById(R.id.btn_save_location)
        dialog.show()
    }
}