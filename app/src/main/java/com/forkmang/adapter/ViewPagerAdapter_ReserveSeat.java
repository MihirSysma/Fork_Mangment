package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.fragment.All_OrderFood_Fragment;
import com.forkmang.fragment.Book_Table_Fragment;
import com.forkmang.fragment.Pickup_Fragment;
import com.forkmang.fragment.Walkin_Fragment;

public class ViewPagerAdapter_ReserveSeat extends FragmentStateAdapter {

    public ViewPagerAdapter_ReserveSeat(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;
        switch (position){
            case 0:
                selectedFragment= All_OrderFood_Fragment.newInstance();
                break;
            case 1:
                selectedFragment= All_OrderFood_Fragment.newInstance();
                break;
            case 2:
                selectedFragment= All_OrderFood_Fragment.newInstance();
                break;
            default:
                return  All_OrderFood_Fragment.newInstance();

        }
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

