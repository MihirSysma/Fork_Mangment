package com.forkmang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.WalkinInQuee;
import com.forkmang.adapter.Pickup_Fragment_BookTableAdapter;

public class Walkin_Fragment extends Fragment {
    private static Walkin_Fragment instance;
    RecyclerView recyclerView;
    LinearLayout get_inquee;
    public static Walkin_Fragment newInstance() {
        return new Walkin_Fragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkin_layout, container, false);

        instance= this;

        get_inquee = view.findViewById(R.id.get_inquee);
        recyclerView = view.findViewById(R.id.walkin_recycleview);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                verticalLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Pickup_Fragment_BookTableAdapter pickup_fragment_bookTableAdapter = new Pickup_Fragment_BookTableAdapter(getActivity());
        recyclerView.setAdapter(pickup_fragment_bookTableAdapter);

        //BookTableAdapter bookTableAdapter = new BookTableAdapter(getActivity());
        //recyclerView.setAdapter(bookTableAdapter);



        get_inquee.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(getContext(), WalkinInQuee.class);
            startActivity(mainIntent);

        });



       return view;

    }


    @Override
    public void onResume() {
        super.onResume();
       // ((Booking_TabView_Activity)getActivity()).hide_search();
    }

    public static Walkin_Fragment GetInstance()
    {
        return instance;
    }
}
