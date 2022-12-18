package com.forkmang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.BookingTable_ReserveSeat;
import com.forkmang.adapter.BookTableAdapter;
import com.forkmang.adapter.CartBookingAdapter;
import com.forkmang.adapter.Pickup_Fragment_BookTableAdapter;

public class Pickup_Fragment extends Fragment {

    RecyclerView recyclerView;

    public static Pickup_Fragment newInstance() {
        return new Pickup_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_layout, container, false);
        recyclerView = view.findViewById(R.id.pick_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Pickup_Fragment_BookTableAdapter pickup_fragment_bookTableAdapter = new Pickup_Fragment_BookTableAdapter(getActivity());
        recyclerView.setAdapter(pickup_fragment_bookTableAdapter);


        return view;

    }




}
