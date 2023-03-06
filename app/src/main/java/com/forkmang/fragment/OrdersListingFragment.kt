package com.forkmang.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapterOrdersListing
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrdersListingFragment : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_orders_layout)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager_orders)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout_orders)
        val viewPagerAdapter_orders =
            ViewPagerAdapterOrdersListing(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter_orders
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.setText(R.string.book_table)
                }
                1 -> {
                    tab.setText(R.string.walkin)
                }
                2 -> {
                    tab.setText(R.string.pickup)
                }
            }
        }.attach()
    } /*public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_layout, container, false);
        viewPager=view.findViewById(R.id.viewPager_orders);
        tabLayout=view.findViewById(R.id.tabLayout_orders);

        ViewPagerAdapter_Orders viewPagerAdapter_orders=new ViewPagerAdapter_Orders(getChildFragmentManager(),getLifecycle());
        viewPager.setAdapter(viewPagerAdapter_orders);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0){
                tab.setText(R.string.book_table);
            }
            else if(position==1){
                tab.setText(R.string.walkin);
            }
            else if(position==2){
                tab.setText(R.string.pickup);
            }
        }).attach();


        return view;

    }
*/
}