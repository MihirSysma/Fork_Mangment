package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;

public class SpinnnerAdapter_Type extends BaseAdapter {
    Context context;
    String[] type;
    LayoutInflater inflter;

    public SpinnnerAdapter_Type(Context applicationContext, String[] type) {
        this.context = applicationContext;
        this.type = type;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return type.length;
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
        person.setText(type[position]);
        return view;
    }
}

