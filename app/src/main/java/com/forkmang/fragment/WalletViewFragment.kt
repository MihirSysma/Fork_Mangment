package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.WalletFragmentAdapter

class WalletViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.wallet_view2, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.wallet_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val walletFragmentAdapter = WalletFragmentAdapter(requireActivity())
        recyclerView.adapter = walletFragmentAdapter
        return view

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