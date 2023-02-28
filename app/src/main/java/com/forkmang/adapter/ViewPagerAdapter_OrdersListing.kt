package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.fragment.Table_OrdersListing_Fragment

class ViewPagerAdapter_OrdersListing(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment = when (position) {
            0 -> Table_OrdersListing_Fragment.newInstance()
            1 -> Table_OrdersListing_Fragment.newInstance()
            2 -> Table_OrdersListing_Fragment.newInstance()
            else -> return Table_OrdersListing_Fragment.newInstance()
        }
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}