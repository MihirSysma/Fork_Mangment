package com.forkmang.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.forkmang.R
import com.forkmang.data.BranchDropdown

class SpinnnerAdapter_Branch(
    var context: Context,
    var branchDropdownArrayList: ArrayList<BranchDropdown>
) : BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return branchDropdownArrayList.size
    }

    override fun getItem(position: Int): Any {
        return branchDropdownArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder", "InflateParams") val view =
            inflter.inflate(R.layout.spinner_cell, null)
        val person = view.findViewById<TextView>(R.id.txt_person)
        val branchDropdown = branchDropdownArrayList[position]
        person.text = branchDropdown.branch_name
        return view
    }
}