package com.forkmang.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forkmang.R;
import com.forkmang.adapter.Wallet_Fragment_Adapter;
import com.forkmang.adapter.location_Fragment_Adapter;

public class LocationScreen_Fragment extends Fragment {

    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_location_screen, container, false);
        recyclerView = view.findViewById(R.id.wallet_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        location_Fragment_Adapter location_fragment_adapter = new location_Fragment_Adapter(getActivity());
        recyclerView.setAdapter(location_fragment_adapter);

        return view;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);

        recyclerView = findViewById(R.id.wallet_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(LocationScreen.this));

        location_Fragment_Adapter location_fragment_adapter = new location_Fragment_Adapter(LocationScreen.this);
        recyclerView.setAdapter(location_fragment_adapter);
    }*/
}