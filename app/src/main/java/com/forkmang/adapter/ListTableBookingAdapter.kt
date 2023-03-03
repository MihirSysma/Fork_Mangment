package com.forkmang.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.activity.BookingTable_DetailView
import com.forkmang.adapter.ListTableBookingAdapter.ListBookTableItemHolder
import com.forkmang.models.TableList

class ListTableBookingAdapter(var activity: Activity, var listTableBooking: ArrayList<TableList>?) :
    RecyclerView.Adapter<ListBookTableItemHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListBookTableItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.table_list_cell, null)
        return ListBookTableItemHolder(v)
    }

    override fun onBindViewHolder(holder: ListBookTableItemHolder, position: Int) {
        val tableList = listTableBooking!![position]
        holder.txt_table_no.text = tableList.table_no
        holder.txt_noperson.text = tableList.number_of_person
        holder.txt_type.text = tableList.type
    }

    override fun getItemCount(): Int {
        return listTableBooking!!.size
    }

    inner class ListBookTableItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_type: TextView
        var txt_noperson: TextView
        var txt_table_no: TextView
        var relative_view: RelativeLayout

        init {
            relative_view = itemView.findViewById(R.id.relative_view)
            txt_type = itemView.findViewById(R.id.txt_type)
            txt_noperson = itemView.findViewById(R.id.txt_noperson)
            txt_table_no = itemView.findViewById(R.id.txt_table_no)
            relative_view.setOnClickListener { v: View? ->
                val position = bindingAdapterPosition
                val tableList = listTableBooking!![position]
                (activity as BookingTable_DetailView).showAlertView_tableselctionrule(tableList)
            }
        }
    }
}