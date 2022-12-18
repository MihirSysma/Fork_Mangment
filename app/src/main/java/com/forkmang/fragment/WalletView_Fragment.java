package com.forkmang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.Wallet_Fragment_Adapter;

public class WalletView_Fragment extends Fragment
{
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_view2, container, false);
        recyclerView = view.findViewById(R.id.wallet_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Wallet_Fragment_Adapter wallet_fragment_adapter = new Wallet_Fragment_Adapter(getActivity());
        recyclerView.setAdapter(wallet_fragment_adapter);

        return view;

    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_view2);
        recyclerView = findViewById(R.id.wallet_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(WalletView.this));

        Wallet_Fragment_Adapter wallet_fragment_adapter = new Wallet_Fragment_Adapter(WalletView.this);
        recyclerView.setAdapter(wallet_fragment_adapter);
    }*/

    }
}
