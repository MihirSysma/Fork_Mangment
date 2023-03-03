package com.forkmang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R

class SpinnnerAdapter_Type_Value(var context: Context, var type_value: Array<String>) :
    BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return type_value.size
    }

    override fun getItem(position: Int): Any {
        return type_value[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder", "InflateParams") val view =
            inflter.inflate(R.layout.spinner_cell, null)
        val person = view.findViewById<TextView>(R.id.txt_person)
        person.text = type_value[position]
        return view
    }
}