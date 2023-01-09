package com.forkmang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.forkmang.R;
import com.forkmang.adapter.ViewPagerAdapter_Contact_Term;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Contact_TermsFragment extends FragmentActivity
{
    ViewPager2 viewPager;
    TabLayout tabLayout;
    RelativeLayout relative_view_nav;
    Activity activity;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = Contact_TermsFragment.this;
        setContentView(R.layout.fragment_contact_term_layout);
        viewPager=findViewById(R.id.viewPager_orders);
        tabLayout=findViewById(R.id.tabLayout_orders);
        relative_view_nav=findViewById(R.id.relative_view_nav);
        ViewPagerAdapter_Contact_Term viewPagerAdapter_contact_term=new ViewPagerAdapter_Contact_Term(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(viewPagerAdapter_contact_term);


//   relative_view_nav.setOnClickListener(v -> ((DashBoardActivity_2) getApplicationContext()).openDrawer());


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0){
                tab.setText(R.string.contact);
            }
            else if(position==1){
                tab.setText(R.string.term);
            }

        }).attach();

    }





}
