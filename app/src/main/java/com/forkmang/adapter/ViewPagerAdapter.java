package com.forkmang.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.fragment.Book_Table_Fragment;
import com.forkmang.fragment.Pickup_Fragment;
import com.forkmang.fragment.Walkin_Fragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    Context ctx;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context ctx) {
        super(fragmentManager, lifecycle);
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;

        switch (position){
            case 0: {
                     selectedFragment = Book_Table_Fragment.newInstance();
                     //((Booking_TabView_Activity)ctx).visble_search();
                }
                break;
            case 1: {
                   selectedFragment = Walkin_Fragment.newInstance();
                   //((Booking_TabView_Activity)ctx).hide_search();
                 }
                break;
            case 2: {
                    selectedFragment = Pickup_Fragment.newInstance();
                    //((Booking_TabView_Activity)ctx).hide_search();
                  }
                break;
            default:
                //((Booking_TabView_Activity)ctx).visble_search();
                return  Book_Table_Fragment.newInstance();

        }
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

