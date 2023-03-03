package com.forkmang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R
import com.forkmang.data.FlooDropdown

class SpinnnerAdapterFloor(
    var context: Context,
    var flooDropdownArrayList: ArrayList<FlooDropdown>
) : BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return flooDropdownArrayList.size
    }

    override fun getItem(position: Int): Any {
        return flooDropdownArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder", "InflateParams") val view =
            inflter.inflate(R.layout.spinner_cell, null)
        val person = view.findViewById<TextView>(R.id.txt_person)
        val flooDropdown = flooDropdownArrayList[position]
        person.text = flooDropdown.floor_name
        return view
    }
}