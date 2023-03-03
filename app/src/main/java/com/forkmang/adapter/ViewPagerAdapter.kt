package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.ViewModel
import com.forkmang.fragment.BookTableFragment
import com.forkmang.fragment.PickupFragment
import com.forkmang.fragment.WalkinListingFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var viewModel: ViewModel
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment = when (position) {
            0 -> {
                BookTableFragment.newInstance(viewModel)
            }
            1 -> {
                WalkinListingFragment.newInstance(viewModel)
            }
            2 -> {
                PickupFragment.newInstance(viewModel)
            }
            else ->                 //((Booking_TabView_Activity)ctx).visble_search();
                return BookTableFragment.newInstance(viewModel)
        }
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}