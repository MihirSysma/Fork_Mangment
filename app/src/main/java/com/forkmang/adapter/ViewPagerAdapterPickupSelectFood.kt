package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.fragment.PickupSelectFoodFragment
import com.forkmang.models.TableList

class ViewPagerAdapterPickupSelectFood(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var foodListArrayList: ArrayList<FoodList_Tab>,
    var tableList: TableList?,
    var restoData: RestoData
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment
        selectedFragment = PickupSelectFoodFragment.newInstance(restoData,foodListArrayList[position].id,restoData.id)
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return foodListArrayList.size
    }
}