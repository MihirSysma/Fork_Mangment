package com.forkmang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forkmang.R;

public class SpinnnerAdapter extends BaseAdapter {
    Context context;
    String[] person_arr;
    LayoutInflater inflter;

    public SpinnnerAdapter(Context applicationContext,  String[] person_arr) {
        this.context = applicationContext;
        this.person_arr = person_arr;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return person_arr.length;
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
        if(position > 0)
        {
            person.setText("Person "+person_arr[position]);
        }
        else{
            person.setText(person_arr[position]);
        }

        return view;
    }
}

