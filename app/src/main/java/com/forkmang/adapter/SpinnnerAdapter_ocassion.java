package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;

public class SpinnnerAdapter_ocassion extends BaseAdapter {
    Context context;
    String[] ocassion_arr;
    LayoutInflater inflter;

    public SpinnnerAdapter_ocassion(Context applicationContext, String[] ocassion_arr) {
        this.context = applicationContext;
        this.ocassion_arr = ocassion_arr;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ocassion_arr.length;
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
        TextView ocassion = view.findViewById(R.id.txt_person);
        if(position > 0)
        {
            ocassion.setText(ocassion_arr[position]);
        }
        else{
            ocassion.setText(ocassion_arr[position]);
        }

        return view;
    }
}

