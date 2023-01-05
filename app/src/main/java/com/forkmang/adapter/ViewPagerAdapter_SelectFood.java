package com.forkmang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.forkmang.data.FoodList_Tab;
import com.forkmang.fragment.Select_Food_Fragment;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;

import java.util.ArrayList;

public class ViewPagerAdapter_SelectFood extends FragmentStateAdapter {
    ArrayList<FoodList_Tab> foodListArrayList;
    TableList tableList;
    BookTable bookTable;

    public ViewPagerAdapter_SelectFood(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<FoodList_Tab> foodListArrayList, TableList tableList, BookTable bookTable) {
        super(fragmentManager, lifecycle);
        this.foodListArrayList = foodListArrayList;
        this.tableList = tableList;
        this.bookTable = bookTable;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment;
        selectedFragment= Select_Food_Fragment.newInstance(tableList,bookTable);
        return  selectedFragment;
    }

    @Override
    public int getItemCount() {
        return foodListArrayList.size();
    }
}

