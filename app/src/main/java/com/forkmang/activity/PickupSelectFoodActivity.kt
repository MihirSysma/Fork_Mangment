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
import com.forkmang.PickUpSelectFoodViewModel
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapterPickupSelectFood
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivitySelectfoodBinding
import com.forkmang.fragment.PickupSelectFoodFragment
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.COMMAND_CART_LIST_VIEW
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
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

class PickupSelectFoodActivity : AppCompatActivity() {

    var ctx: Context = this@PickupSelectFoodActivity
    var foodListArrayList: ArrayList<FoodList_Tab>? = null
    private lateinit var viewPagerAdapterReserveSeat: ViewPagerAdapterPickupSelectFood
    var restoData: RestoData? = null
    var booking_id: String? = null
    var category_id: String? = null
    var current_tabactive: Int = 0

    private val binding by lazy { ActivitySelectfoodBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[PickUpSelectFoodViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.linearView1.visibility = View.GONE
        binding.linearView2.visibility = View.GONE
        binding.linearView3.visibility = View.GONE
        binding.txtBooktable.text = "Pickup Table"
        binding.imgEdit.setOnClickListener { finish() }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        //tableList = (TableList) getIntent().getSerializableExtra("table_model");

        /*String timedate = getIntent().getStringExtra("timedate");
        String day = getIntent().getStringExtra("day");
        String noseat = getIntent().getStringExtra("noseat");

        txt_datetime.setText(timedate);
        txt_day.setText(day);
        txt_noofseat.setText(noseat+" "+"Seats");
        txt_view_day.setText(day);
        txt_view_day_2.setText(day);*/
        binding.txtrestroname.text = restoData?.rest_name
        binding.txtTime.text = restoData?.endtime
        binding.txtTotalkm.text = restoData?.distance + " km"
        booking_id = restoData?.id
        binding.imgSearchicon.setOnClickListener {
            val str_search: String = binding.etvSearchview.text.toString()
            if (Utils.isNetworkAvailable(ctx)) {
                viewModel.callApiSearchFoodItem(category_id, str_search)
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        binding.etvSearchview.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.toString().isEmpty()) {
                    Hidekeyboard()
                    if (Utils.isNetworkAvailable(ctx)) {
                        viewModel.callApiFoodItem(category_id)
                    } else {
                        showToastMessage(Constant.NETWORKEROORMSG)
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
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                current_tabactive = position
                val foodList_tab: FoodList_Tab? = foodListArrayList?.get(position)
                category_id = foodList_tab?.id
            }
        })
        if (Utils.isNetworkAvailable(ctx)) {
            callapi_tablisting("1")
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    private fun fill_tablist() {
        TabLayoutMediator(
            (binding.tabLayout),
            (binding.viewPager)
        ) { tab: TabLayout.Tab, position: Int ->
            for (i in foodListArrayList?.indices!!) {
                val foodList_tab: FoodList_Tab? = foodListArrayList?.get(position)
                //tab.setText(foodList_tab.getName().toLowerCase());
                tab.customView = getTabView(foodList_tab?.name?.lowercase(Locale.getDefault()))
                //TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                //tabOne.setText(foodList_tab.getName());
                //tabLayout.getTabAt(i).setCustomView(tabOne);
                //tabOne.setText(foodList_tab.getName().trim().toLowerCase());
                //tab.setCustomView(tabOne);
                //tabLayout.addTab(tabLayout.newTab().setText(foodList_tab.getName().trim().toLowerCase()));
            }
        }.attach()
    }

    private fun showAlertView() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PickupSelectFoodActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_tablefood_reservealert, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val btn_share_order: Button = dialogView.findViewById(R.id.btn_share_order)
        btn_share_order.setOnClickListener {
            val intent = Intent(
                this@PickupSelectFoodActivity,
                BookingOrderReserverConformationActivity::class.java
            )
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun callapi_tablisting(branch_id: String) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        info.getres_foodlist(branch_id)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    //Log.d("Result", jsonObject.toString());
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                foodListArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val foodList_tab = FoodList_Tab()
                                    val mjson_obj: JSONObject =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    foodList_tab.name = mjson_obj.getString("name")
                                    foodList_tab.id = mjson_obj.getString("id")
                                    foodListArrayList?.add(foodList_tab)
                                }
                                binding.progressBar.visibility = View.GONE
                                viewPagerAdapterReserveSeat =
                                    ViewPagerAdapterPickupSelectFood(
                                        supportFragmentManager,
                                        lifecycle,
                                        foodListArrayList!!,
                                        restoData!!,
                                        viewModel
                                    )
                                binding.viewPager.adapter = viewPagerAdapterReserveSeat
                                fill_tablist()
                            }
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        //val jsonObject: JSONObject = JSONObject(response.errorBody()?.string())
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage("Error occur please try again")
                //stopProgress();
            }
        })
    }

    private fun Hidekeyboard() {
        binding.etvSearchview.clearFocus()
        val `in`: InputMethodManager = this@PickupSelectFoodActivity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.etvSearchview.windowToken, 0)
    }

    fun getTabView(str: String?): View {
        val tab: View =
            LayoutInflater.from(this@PickupSelectFoodActivity).inflate(R.layout.custom_tab, null)
        val tv: TextView = tab.findViewById(R.id.custom_text)
        tv.text = str
        return tab
    }
}