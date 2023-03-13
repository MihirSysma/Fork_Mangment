package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.forkmang.R
import com.forkmang.vm.SelectFoodViewModel
import com.forkmang.adapter.ViewPagerAdapterSelectFood
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivitySelectfoodBinding
import com.forkmang.helper.Constant.COMMAND_CART_LIST_VIEW
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.ERROR_CODE
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.SUCCESS_CODE
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class SelectFoodActivity : AppCompatActivity() {

    var ctx: Context = this@SelectFoodActivity
    var foodListArrayList: ArrayList<FoodList_Tab>? = null
    var restoData: RestoData? = null
    var tableList: TableList? = null
    var bookingId: String? = null
    var categoryId: String? = null
    var currentTabActive: Int = 0

    private val binding by lazy { ActivitySelectfoodBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[SelectFoodViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgEdit.setOnClickListener { finish() }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        tableList = intent.getSerializableExtra("table_model") as TableList?
        val timeDate: String? = intent.getStringExtra("timedate")
        val day: String? = intent.getStringExtra("day")
        val noSeat: String? = intent.getStringExtra("noseat")
        binding.txtDatetime.text = timeDate
        binding.txtDay.text = day
        binding.txtNoofseat.text = "$noSeat Seats"
        binding.txtViewDay.text = day
        binding.txtViewDay2.text = day
        binding.txtrestroname.text = restoData?.rest_name
        binding.txtTime.text = restoData?.endtime
        binding.txtTotalkm.text = restoData?.distance + " km"
        bookingId = restoData?.id
        binding.imgSearchicon.setOnClickListener {
            val strSearch: String = binding.etvSearchview.text.toString()
            if (Utils.isNetworkAvailable(ctx)) {
                viewModel.callApiSearchFoodItem(categoryId, strSearch)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        binding.etvSearchview.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
                if (s.toString().isEmpty()) {
                    hidekeyboard()
                    if (Utils.isNetworkAvailable(ctx)) {
                        viewModel.callApiFoodItem(categoryId)
                    } else {
                        showToastMessage(NETWORKEROORMSG)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

                // TODO Auto-generated method stub
            }
        })
        binding.btnViewCart.setOnClickListener {
            //showAlertView();
            viewModel.command.postValue(COMMAND_CART_LIST_VIEW)
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageno", "" + position)
                currentTabActive = position
                val foodListTab: FoodList_Tab? = foodListArrayList?.get(position)
                categoryId = foodListTab?.id
                if (Utils.isNetworkAvailable(this@SelectFoodActivity)) {
                    viewModel.callApiFoodItem(categoryId)
                } else {
                    showToastMessage(NETWORKEROORMSG)
                }
            }
        })
        if (Utils.isNetworkAvailable(ctx)) {
            callApiTabListing("1")
        } else {
            showToastMessage(NETWORKEROORMSG)
        }
    }

    private fun fillTabList() {
        TabLayoutMediator(
            (binding.tabLayout),
            (binding.viewPager)
        ) { tab: TabLayout.Tab, position: Int ->
            for (i in foodListArrayList!!.indices) {
                val foodListTab: FoodList_Tab? = foodListArrayList?.get(position)
                //tab.setText(foodList_tab.getName());
                tab.customView = getTabView(foodListTab?.name?.lowercase(Locale.getDefault()))
            }
        }.attach()
    }

    private fun showAlertView() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@SelectFoodActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_tablefood_reservealert, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val btnShareOrder: Button = dialogView.findViewById(R.id.btn_share_order)
        btnShareOrder.setOnClickListener {
            val intent = Intent(
                this@SelectFoodActivity,
                BookingOrderReserverConformationActivity::class.java
            )
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun callApiTabListing(branch_id: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.getResFoodList(branch_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    if (response.code() == SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                foodListArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val foodListTab = FoodList_Tab()
                                    val mjsonObj: JSONObject =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    foodListTab.name = mjsonObj.getString("name")
                                    foodListTab.id = mjsonObj.getString("id")
                                    foodListArrayList?.add(foodListTab)
                                }
                                binding.progressBar.visibility = View.GONE
                                val viewPagerAdapterReserveSeat =
                                    ViewPagerAdapterSelectFood(
                                        supportFragmentManager,
                                        lifecycle,
                                        foodListArrayList!!,
                                        tableList!!,
                                        restoData!!,
                                        viewModel
                                    )
                                binding.viewPager.adapter = viewPagerAdapterReserveSeat
                                fillTabList()
                            }
                        }
                    } else if (response.code() == ERROR_CODE) {
                        //val jsonObject = JSONObject(response.errorBody()!!.string())
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(ERRORMSG)
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun hidekeyboard() {
        binding.etvSearchview.clearFocus()
        val `in`: InputMethodManager = this@SelectFoodActivity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.etvSearchview.windowToken, 0)
    }

    private fun getTabView(str: String?): View {
        val tab: View =
            LayoutInflater.from(this@SelectFoodActivity).inflate(R.layout.custom_tab, null)
        val tv: TextView = tab.findViewById(R.id.custom_text)
        tv.text = str
        return tab
    }
}