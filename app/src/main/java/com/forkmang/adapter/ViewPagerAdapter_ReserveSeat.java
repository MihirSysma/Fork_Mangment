package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.data.FoodList_Tab;
import com.forkmang.fragment.All_Food_Fragment;

import java.util.ArrayList;

public class ViewPagerAdapter_ReserveSeat extends FragmentStateAdapter {
    ArrayList<FoodList_Tab> foodListArrayList;

    public ViewPagerAdapter_ReserveSeat(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,ArrayList<FoodList_Tab> foodListArrayList) {
        super(fragmentManager, lifecycle);
        this.foodListArrayList = foodListArrayList;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;
        selectedFragment= All_Food_Fragment.newInstance();

        /*switch (position){
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


        }*/
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return foodListArrayList.size();
    }
}

