package com.forkmang.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.ViewModel
import com.forkmang.fragment.Book_Table_Fragment
import com.forkmang.fragment.Pickup_Fragment
import com.forkmang.fragment.Walkin_listing_Fragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var ctx: Context,
    var viewModel: ViewModel
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment = when (position) {
            0 -> {
                Book_Table_Fragment.newInstance(viewModel)
            }
            1 -> {
                Walkin_listing_Fragment.newInstance(viewModel)
            }
            2 -> {
                Pickup_Fragment.newInstance(viewModel)
            }
            else ->                 //((Booking_TabView_Activity)ctx).visble_search();
                return Book_Table_Fragment.newInstance(viewModel)
        }
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}