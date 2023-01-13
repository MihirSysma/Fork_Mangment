package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;
import com.forkmang.data.BranchDropdown;

import java.util.ArrayList;

public class SpinnnerAdapter_Branch extends BaseAdapter {
    Context context;
    ArrayList<BranchDropdown> branchDropdownArrayList;
    LayoutInflater inflter;

    public SpinnnerAdapter_Branch(Context applicationContext, ArrayList<BranchDropdown> branchDropdownArrayList) {
        this.context = applicationContext;
        this.branchDropdownArrayList = branchDropdownArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return branchDropdownArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        @SuppressLint({"ViewHolder", "InflateParams"})
        View view = inflter.inflate(R.layout.spinner_cell, null);
        TextView person = view.findViewById(R.id.txt_person);
        BranchDropdown branchDropdown = branchDropdownArrayList.get(position);
        person.setText(branchDropdown.getBranch_name());
        return view;
    }
}

