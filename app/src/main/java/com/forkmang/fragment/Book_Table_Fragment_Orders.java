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
import com.forkmang.adapter.BookTableAdapter;
import com.forkmang.adapter.BookTableAdapter_Orders;

public class Book_Table_Fragment_Orders extends Fragment {
    RecyclerView recyclerView;

    public static Book_Table_Fragment_Orders newInstance() {
        return new Book_Table_Fragment_Orders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booktable_layout_orders, container, false);
        recyclerView = view.findViewById(R.id.booktable_recycleview_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BookTableAdapter_Orders bookTableAdapter_orders = new BookTableAdapter_Orders(getActivity());
        recyclerView.setAdapter(bookTableAdapter_orders);

        return view;

    }




}
