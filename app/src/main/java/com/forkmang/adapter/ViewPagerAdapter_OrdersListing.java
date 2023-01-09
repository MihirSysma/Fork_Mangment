package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.fragment.Table_OrdersListing_Fragment;

public class ViewPagerAdapter_OrdersListing extends FragmentStateAdapter {

    public ViewPagerAdapter_OrdersListing(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;

        switch (position){
            case 0:
                selectedFragment= Table_OrdersListing_Fragment.newInstance();
                break;
            case 1:
                selectedFragment= Table_OrdersListing_Fragment.newInstance();
                break;
            case 2:
                selectedFragment= Table_OrdersListing_Fragment.newInstance();
                break;

            default:
                return  Table_OrdersListing_Fragment.newInstance();

        }
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

