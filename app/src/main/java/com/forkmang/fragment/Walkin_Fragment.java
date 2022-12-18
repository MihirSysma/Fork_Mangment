package com.forkmang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.LoginActivity;
import com.forkmang.activity.RegisterActivity;
import com.forkmang.activity.WalkinInQuee;
import com.forkmang.adapter.BookTableAdapter;

public class Walkin_Fragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout get_inquee;
    public static Walkin_Fragment newInstance() {
        return new Walkin_Fragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkin_layout, container, false);
        get_inquee = view.findViewById(R.id.get_inquee);
        recyclerView = view.findViewById(R.id.walkin_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        BookTableAdapter bookTableAdapter = new BookTableAdapter(getActivity());
        recyclerView.setAdapter(bookTableAdapter);


        get_inquee.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(getContext(), WalkinInQuee.class);
            startActivity(mainIntent);

        });



       return view;

    }




}
