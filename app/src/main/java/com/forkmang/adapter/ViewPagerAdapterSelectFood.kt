package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.SelectFoodViewModel
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.fragment.SelectFoodFragment
import com.forkmang.models.TableList

class ViewPagerAdapterSelectFood(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var foodListArrayList: ArrayList<FoodList_Tab>,
    var tableList: TableList,
    var restoData: RestoData,
    var viewModel: SelectFoodViewModel
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment
        selectedFragment = SelectFoodFragment.newInstance(tableList, restoData, viewModel)
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return foodListArrayList.size
    }
}