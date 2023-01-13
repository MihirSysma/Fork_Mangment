package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;
import com.forkmang.data.AreaDropdown;

import java.util.ArrayList;

public class SpinnnerAdapter_Type extends BaseAdapter {
    Context context;
    ArrayList<AreaDropdown> areaDropdownArrayList;
    LayoutInflater inflter;

    public SpinnnerAdapter_Type(Context applicationContext, ArrayList<AreaDropdown> areaDropdownArrayList) {
        this.context = applicationContext;
        this.areaDropdownArrayList = areaDropdownArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return areaDropdownArrayList.size();
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
        AreaDropdown areaDropdown = areaDropdownArrayList.get(position);
        person.setText(areaDropdown.getArea_name());
        person.setTag(areaDropdown.getId());
        return view;
    }
}

