package com.forkmang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.forkmang.R;
import com.forkmang.activity.Booking_TabView_Activity;

public class ScanOrderFragment extends Fragment {
    TextView txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanorder_layout, container, false);
        txt = view.findViewById(R.id.txt);
        txt.setOnClickListener(v -> {
            Log.d("HI", "Hello");
            Intent intent = new Intent(getActivity(), Booking_TabView_Activity.class);
            intent.putExtra("tab_no", "0");
            startActivity(intent);
        });

       return view;



    }




}
