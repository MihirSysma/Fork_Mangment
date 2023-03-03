package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.fragment.TableOrdersListingFragment

class ViewPagerAdapterOrdersListing(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment = when (position) {
            0 -> TableOrdersListingFragment.newInstance()
            1 -> TableOrdersListingFragment.newInstance()
            2 -> TableOrdersListingFragment.newInstance()
            else -> return TableOrdersListingFragment.newInstance()
        }
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}