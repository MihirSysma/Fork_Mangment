package com.forkmang.fragment

import android.app.Activity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapterContactTerm
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContactTermsFragment : FragmentActivity() {

    var relative_view_nav: RelativeLayout? = null
    var activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this@ContactTermsFragment
        setContentView(R.layout.fragment_contact_term_layout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager_orders)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout_orders)
        relative_view_nav = findViewById(R.id.relative_view_nav)
        val viewPagerAdapter_contact_term =
            ViewPagerAdapterContactTerm(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter_contact_term


//   relative_view_nav.setOnClickListener(v -> ((DashBoardActivity_2) getApplicationContext()).openDrawer());
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.setText(R.string.contact)
            } else if (position == 1) {
                tab.setText(R.string.term)
            }
        }.attach()
    }
}