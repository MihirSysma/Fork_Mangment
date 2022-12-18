package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.fragment.Book_Table_Fragment_Orders;
import com.forkmang.fragment.Contact_Fragment;
import com.forkmang.fragment.Term_Fragment;

public class ViewPagerAdapter_Contact_Term extends FragmentStateAdapter {

    public ViewPagerAdapter_Contact_Term(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;

        switch (position){
            case 0:
                selectedFragment= Contact_Fragment.newInstance();
                break;
            case 1:
                selectedFragment= Term_Fragment.newInstance();
                break;
            default:
                return  Contact_Fragment.newInstance();

        }
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

