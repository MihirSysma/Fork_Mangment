package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.data.FoodList_Tab;
import com.forkmang.data.RestoData;
import com.forkmang.fragment.PickupSelect_Food_Fragment;
import com.forkmang.models.TableList;

import java.util.ArrayList;

public class ViewPagerAdapter_PickupSelectFood extends FragmentStateAdapter {
    ArrayList<FoodList_Tab> foodListArrayList;
    TableList tableList;
    RestoData restoData;

    public ViewPagerAdapter_PickupSelectFood(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<FoodList_Tab> foodListArrayList, TableList tableList, RestoData restoData) {
        super(fragmentManager, lifecycle);
        this.foodListArrayList = foodListArrayList;
        this.tableList = tableList;
        this.restoData = restoData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;
        selectedFragment= PickupSelect_Food_Fragment.newInstance(restoData);
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return foodListArrayList.size();
    }
}

