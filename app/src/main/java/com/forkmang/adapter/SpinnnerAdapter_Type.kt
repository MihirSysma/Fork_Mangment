package com.forkmang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R
import com.forkmang.data.AreaDropdown

class SpinnnerAdapter_Type(
    var context: Context,
    var areaDropdownArrayList: ArrayList<AreaDropdown>
) : BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return areaDropdownArrayList.size
    }

    override fun getItem(position: Int): Any {
        return areaDropdownArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        @SuppressLint("ViewHolder", "InflateParams") val view =
            inflter.inflate(R.layout.spinner_cell, null)
        val person = view.findViewById<TextView>(R.id.txt_person)
        val areaDropdown = areaDropdownArrayList[position]
        person.text = areaDropdown.area_name
        person.tag = areaDropdown.id
        return view
    }
}