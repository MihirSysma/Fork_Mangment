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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapterSelectFood
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivitySelectfoodBinding
import com.forkmang.fragment.SelectFoodFragment
import com.forkmang.helper.Constant
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
    var booking_id: String? = null
    var category_id: String? = null
    var current_tabactive: Int = 0

    private val binding by lazy { ActivitySelectfoodBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgEdit.setOnClickListener { finish() }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        tableList = intent.getSerializableExtra("table_model") as TableList?
        val timedate: String? = intent.getStringExtra("timedate")
        val day: String? = intent.getStringExtra("day")
        val noseat: String? = intent.getStringExtra("noseat")
        binding.txtDatetime.text = timedate
        binding.txtDay.text = day
        binding.txtNoofseat.text = "$noseat Seats"
        binding.txtViewDay.text = day
        binding.txtViewDay2.text = day
        binding.txtrestroname.text = restoData?.rest_name
        binding.txtTime.text = restoData?.endtime
        binding.txtTotalkm.text = restoData?.distance + " km"
        booking_id = restoData?.id
        binding.imgSearchicon.setOnClickListener {
            val str_search: String = binding.etvSearchview.text.toString()
            val all_Food_fragment = SelectFoodFragment()
            if (Utils.isNetworkAvailable(ctx)) {
                //TODO: redo this code, should not call frag instance
                all_Food_fragment.callApiSearchFoodItem(category_id, str_search)
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
                // TODO Auto-generated method stub
                if (s.toString().isEmpty()) {
                    hidekeyboard()
                    if (Utils.isNetworkAvailable(ctx)) {
                        //TODO: redo this code, should not call frag instance
                        val all_Food_fragment = SelectFoodFragment()
                        all_Food_fragment.callApiFoodItem(category_id)
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
            //TODO: redo this code, should not call frag instance

            val all_Food_fragment = SelectFoodFragment()
            all_Food_fragment.cartListingView()
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageno", "" + position)
                current_tabactive = position
                val foodList_tab: FoodList_Tab = foodListArrayList!![position]
                category_id = foodList_tab.id
                //TODO: redo this code, should not call frag instance
                val all_Food_fragment = SelectFoodFragment()
                booking_id?.let { all_Food_fragment.callApi_food_1(category_id, it) }
            }
        })
        if (Utils.isNetworkAvailable(ctx)) {
            callApiTabListing("1")
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    private fun fillTabList() {
        TabLayoutMediator(
            (binding.tabLayout),
            (binding.viewPager)
        ) { tab: TabLayout.Tab, position: Int ->
            for (i in foodListArrayList!!.indices) {
                val foodList_tab: FoodList_Tab = foodListArrayList!![position]
                //tab.setText(foodList_tab.getName());
                tab.customView = getTabView(foodList_tab.name!!.lowercase(Locale.getDefault()))
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
        val btn_share_order: Button = dialogView.findViewById(R.id.btn_share_order)
        btn_share_order.setOnClickListener {
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
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            if (jsonObject.getJSONObject("data").getJSONArray("data")
                                    .length() > 0
                            ) {
                                foodListArrayList = ArrayList()
                                for (i in 0 until jsonObject.getJSONObject("data")
                                    .getJSONArray("data").length()) {
                                    val foodList_tab: FoodList_Tab = FoodList_Tab()
                                    val mjson_obj: JSONObject =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    foodList_tab.name = mjson_obj.getString("name")
                                    foodList_tab.id = mjson_obj.getString("id")
                                    foodListArrayList!!.add(foodList_tab)
                                }
                                binding.progressBar.visibility = View.GONE
                                val viewPagerAdapter_reserveSeat =
                                    ViewPagerAdapterSelectFood(
                                        supportFragmentManager,
                                        lifecycle,
                                        foodListArrayList!!,
                                        tableList!!,
                                        restoData!!
                                    )
                                binding.viewPager.adapter = viewPagerAdapter_reserveSeat
                                fillTabList()
                            }
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
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