package com.forkmang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R

class SpinnerAdapter(var context: Context, var person_arr: Array<String>) : BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return person_arr.size
    }

    override fun getItem(position: Int): Any {
        return person_arr[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = inflter.inflate(R.layout.spinner_cell, null)
        val person = view.findViewById<TextView>(R.id.txt_person)
        if (position > 0) {
            person.text = "Person " + person_arr[position]
        } else {
            person.text = person_arr[position]
        }
        return view
    }
}