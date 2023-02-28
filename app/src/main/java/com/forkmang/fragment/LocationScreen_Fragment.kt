package com.forkmang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.location_Fragment_Adapter

class LocationScreen_Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_location_screen, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.wallet_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val location_fragment_adapter = location_Fragment_Adapter(requireActivity())
        recyclerView.adapter = location_fragment_adapter
        return view
    } /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);

        recyclerView = findViewById(R.id.wallet_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(LocationScreen.this));

        location_Fragment_Adapter location_fragment_adapter = new location_Fragment_Adapter(LocationScreen.this);
        recyclerView.setAdapter(location_fragment_adapter);
    }*/
}