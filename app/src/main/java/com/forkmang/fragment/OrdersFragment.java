package com.forkmang.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter_Orders;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrdersFragment extends FragmentActivity
{
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_orders_layout);
        viewPager=findViewById(R.id.viewPager_orders);
        tabLayout=findViewById(R.id.tabLayout_orders);

        ViewPagerAdapter_Orders viewPagerAdapter_orders=new ViewPagerAdapter_Orders(getSupportFragmentManager(),getLifecycle());
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

    }


    /*public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
