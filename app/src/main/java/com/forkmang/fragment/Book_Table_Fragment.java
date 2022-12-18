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
import com.forkmang.activity.Booking_TabView_Activity;
import com.forkmang.adapter.BookTableAdapter;

public class Book_Table_Fragment extends Fragment {
    RecyclerView recyclerView;

    public static Book_Table_Fragment newInstance() {
        return new Book_Table_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booktable_layout, container, false);
        recyclerView = view.findViewById(R.id.booktable_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BookTableAdapter bookTableAdapter = new BookTableAdapter(getActivity());
        recyclerView.setAdapter(bookTableAdapter);
        return view;

    }




}
