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
import androidx.viewpager2.widget.ViewPager2
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapter
import com.forkmang.databinding.ActivityBookingTabViewBinding
import com.forkmang.fragment.Book_Table_Fragment
import com.forkmang.fragment.Pickup_Fragment
import com.forkmang.fragment.Walkin_listing_Fragment
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Booking_TabView_Activity : AppCompatActivity() {
    var activity: Activity = this@Booking_TabView_Activity
    var current_tabactive = 0
    var storePrefrence: StorePrefrence? = null
    var longitude = 0.0
    var c_longitude = 0.0
    var c_latitude = 0.0
    var latitude = 0.0

    private val binding by lazy { ActivityBookingTabViewBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgLoc.setOnClickListener {
            val intent_walkin = Intent(this@Booking_TabView_Activity, MapsActivity::class.java)
            startActivity(intent_walkin)
        }
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageno", "" + position)
                current_tabactive = position
            }
        })
        val str_tab_no = intent.getStringExtra("tab_no")
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.setText(R.string.book_table)
            } else if (position == 1) {
                tab.setText(R.string.walkin)
            } else if (position == 2) {
                tab.setText(R.string.pickup)
            }
        }.attach()
        selectPage(str_tab_no!!.toInt())

        /*if(Constant.IS_BookTableFragmentLoad)
        {
            etv_serach.setEnabled(true);
        }
        else{
            etv_serach.setEnabled(false);
        }*/binding.etvSerach.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
                if (current_tabactive == 0) {
                    val book_table_fragment = Book_Table_Fragment()
                    //Book Table Fragment
                    if (s.toString().isEmpty()) {
                        Hidekeyboard()
                        book_table_fragment.call_reloadbooktable()
                    } else if (s.toString().length > 3) {
                        Hidekeyboard()
                        book_table_fragment.filter_booktable(binding.etvSerach.text.toString())
                    }
                } else if (current_tabactive == 1) {
                    val walkin_listing_fragment = Walkin_listing_Fragment()
                    //walking fragment
                    if (s.toString().isEmpty()) {
                        Hidekeyboard()
                        walkin_listing_fragment.call_reloadbooktable()
                    } else if (s.toString().length > 3) {
                        Hidekeyboard()
                        walkin_listing_fragment.filter_booktable(binding.etvSerach.text.toString())
                    }
                } else if (current_tabactive == 3) {
                    val pickup_fragment = Pickup_Fragment()
                    //walking fragment
                    if (s.toString().isEmpty()) {
                        Hidekeyboard()
                        pickup_fragment.call_reloadbooktable()
                    } else if (s.toString().length > 3) {
                        Hidekeyboard()
                        pickup_fragment.filter_booktable(binding.etvSerach.text.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
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

    fun hide_search() {
        binding.etvSerach.visibility = View.GONE
    }

    fun visble_search() {
        binding.etvSerach.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        storePrefrence = StorePrefrence(this@Booking_TabView_Activity)
        if (storePrefrence!!.getCoordinates(Constant.KEY_LATITUDE) == "0.0" || storePrefrence!!.getCoordinates(
                Constant.KEY_LONGITUDE
            ) == "0.0"
        ) {
            longitude = 0.0
            latitude = 0.0
        } else {
            longitude = storePrefrence!!.getCoordinates(Constant.KEY_LONGITUDE)!!.toDouble()
            latitude = storePrefrence!!.getCoordinates(Constant.KEY_LATITUDE)!!.toDouble()
        }
    }
}