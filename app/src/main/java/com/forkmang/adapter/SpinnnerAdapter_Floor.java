package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;
import com.forkmang.data.FlooDropdown;

import java.util.ArrayList;

public class SpinnnerAdapter_Floor extends BaseAdapter {
    Context context;
    ArrayList<FlooDropdown> flooDropdownArrayList;
    LayoutInflater inflter;

    public SpinnnerAdapter_Floor(Context applicationContext, ArrayList<FlooDropdown> flooDropdownArrayList) {
        this.context = applicationContext;
        this.flooDropdownArrayList = flooDropdownArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flooDropdownArrayList.size();
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
        FlooDropdown flooDropdown = flooDropdownArrayList.get(position);
        person.setText(flooDropdown.getFloor_name());
        return view;
    }
}

