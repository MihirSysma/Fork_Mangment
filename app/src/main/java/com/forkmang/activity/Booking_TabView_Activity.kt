package com.forkmang.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.forkmang.R
import com.forkmang.ViewModel
import com.forkmang.adapter.ViewPagerAdapter
import com.forkmang.databinding.ActivityBookingTabViewBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.logThis
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Booking_TabView_Activity : AppCompatActivity() {
    var activity: Activity = this@Booking_TabView_Activity
    var storePrefrence: StorePrefrence? = null
    var longitude = 0.0
    var c_longitude = 0.0
    var c_latitude = 0.0
    var latitude = 0.0
    private val viewModel by lazy { ViewModelProvider(this) [ViewModel::class.java] }

    private val binding by lazy { ActivityBookingTabViewBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgLoc.setOnClickListener {
            val intent_walkin = Intent(this@Booking_TabView_Activity, MapsActivity::class.java)
            startActivity(intent_walkin)
        }
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, this, viewModel)
        binding.viewPager.adapter = viewPagerAdapter

        val str_tab_no = intent.getStringExtra("tab_no")
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = when (position) {
                0 -> getString(R.string.book_table)
                1 -> getString(R.string.walkin)
                2 -> getString(R.string.pickup)
                else -> "TAB"
            }
        }.attach()
        selectPage(str_tab_no?.toInt()?:1)

        /*if(Constant.IS_BookTableFragmentLoad)
        {
            etv_serach.setEnabled(true);
        }
        else{
            etv_serach.setEnabled(false);
        }*/
        binding.etvSerach.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.searchData.postValue(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
        })
        binding.etvSerach.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Hidekeyboard()
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun selectPage(pageIndex: Int) {
        Log.d("tabno=>", "" + pageIndex)
        binding.tabLayout.setScrollPosition(pageIndex, 0f, true)
        binding.viewPager.currentItem = pageIndex
    }

    private fun Hidekeyboard() {
        binding.etvSerach.clearFocus()
        val `in` = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.etvSerach.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        storePrefrence = StorePrefrence(this@Booking_TabView_Activity)
        if (storePrefrence?.getCoordinates(Constant.KEY_LATITUDE) == "0.0" || storePrefrence?.getCoordinates(
                Constant.KEY_LONGITUDE
            ) == "0.0"
        ) {
            longitude = 0.0
            latitude = 0.0
        } else {
            longitude = storePrefrence?.getCoordinates(Constant.KEY_LONGITUDE)?.toDouble()?: 0.0
            latitude = storePrefrence?.getCoordinates(Constant.KEY_LATITUDE)?.toDouble()?: 0.0
        }
    }
}