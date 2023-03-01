package com.forkmang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R

class SpinnnerAdapter_ocassion(var context: Context, var ocassion_arr: Array<String>) :
    BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return ocassion_arr.size
    }

    override fun getItem(position: Int): Any {
        return ocassion_arr[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder", "InflateParams") val view =
            inflter.inflate(R.layout.spinner_cell, null)
        val ocassion = view.findViewById<TextView>(R.id.txt_person)
        if (position > 0) {
            ocassion.text = ocassion_arr[position]
        } else {
            ocassion.text = ocassion_arr[position]
        }
        return view
    }
}