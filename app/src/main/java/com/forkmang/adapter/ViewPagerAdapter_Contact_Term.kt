package com.forkmang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.forkmang.fragment.Contact_Fragment
import com.forkmang.fragment.Term_Fragment

class ViewPagerAdapter_Contact_Term(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val selectedFragment: Fragment = when (position) {
            0 -> Contact_Fragment.newInstance()
            1 -> Term_Fragment.newInstance()
            else -> return Contact_Fragment.newInstance()
        }
        return selectedFragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}