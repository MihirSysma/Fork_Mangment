package com.forkmang.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.forkmang.R
import com.forkmang.vm.ViewModel
import com.forkmang.adapter.ViewPagerAdapter
import com.forkmang.databinding.ActivityBookingTabViewBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePreference
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BookingTabViewActivity : AppCompatActivity() {

    var activity: Activity = this@BookingTabViewActivity
    private val storePreference by lazy { StorePreference(this) }
    var longitude = 0.0
    var latitude = 0.0

    private val viewModel by lazy { ViewModelProvider(this)[ViewModel::class.java] }
    private val binding by lazy { ActivityBookingTabViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgLoc.setOnClickListener {
            val intentWalkin = Intent(this@BookingTabViewActivity, MapsActivity::class.java)
            startActivity(intentWalkin)
        }
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, viewModel)
        binding.viewPager.adapter = viewPagerAdapter

        val strTabNo = intent.getStringExtra("tab_no")
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
        selectPage(strTabNo?.toInt() ?: 1)

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
                hidekeyboard()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun selectPage(pageIndex: Int) {
        Log.d("tabno=>", "" + pageIndex)
        binding.tabLayout.setScrollPosition(pageIndex, 0f, true)
        binding.viewPager.currentItem = pageIndex
    }

    private fun hidekeyboard() {
        binding.etvSerach.clearFocus()
        val `in` = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.etvSerach.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        if (storePreference.getCoordinates(Constant.KEY_LATITUDE) == "0.0" || storePreference.getCoordinates(
                Constant.KEY_LONGITUDE
            ) == "0.0"
        ) {
            longitude = 0.0
            latitude = 0.0
        } else {
            longitude = storePreference.getCoordinates(Constant.KEY_LONGITUDE)?.toDouble() ?: 0.0
            latitude = storePreference.getCoordinates(Constant.KEY_LATITUDE)?.toDouble() ?: 0.0
        }
    }
}