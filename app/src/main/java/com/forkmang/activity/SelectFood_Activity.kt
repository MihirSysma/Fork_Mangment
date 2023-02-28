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
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapter_SelectFood
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivitySelectfoodBinding
import com.forkmang.fragment.Select_Food_Fragment
import com.forkmang.helper.Constant
import com.forkmang.helper.Utils
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class SelectFood_Activity : AppCompatActivity() {
    var btn_view_cart: Button? = null
    var ctx: Context = this@SelectFood_Activity
    var foodListArrayList: ArrayList<FoodList_Tab>? = null
    var restoData: RestoData? = null
    var tableList: TableList? = null
    var booking_id: String? = null
    var category_id: String? = null
    var current_tabactive: Int = 0
    var etv_searchview: EditText? = null

    private val binding by lazy { ActivitySelectfoodBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        btn_view_cart = findViewById(R.id.btn_view_cart)
        val img_searchicon: ImageView = findViewById(R.id.img_searchicon)
        val img_edit: ImageView = findViewById(R.id.img_edit)
        etv_searchview = findViewById(R.id.etv_searchview)
        val txtrestroname: TextView = findViewById(R.id.txtrestroname)
        val txt_time: TextView = findViewById(R.id.txt_time)
        val txt_totalkm: TextView = findViewById(R.id.txt_totalkm)
        val txt_datetime: TextView = findViewById(R.id.txt_datetime)
        val txt_day: TextView = findViewById(R.id.txt_day)
        val txt_noofseat: TextView = findViewById(R.id.txt_noofseat)
        val txt_view_day: TextView = findViewById(R.id.txt_view_day)
        val txt_view_day_2: TextView = findViewById(R.id.txt_view_day_2)
        img_edit.setOnClickListener { finish() }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        tableList = intent.getSerializableExtra("table_model") as TableList?
        val timedate: String? = intent.getStringExtra("timedate")
        val day: String? = intent.getStringExtra("day")
        val noseat: String? = intent.getStringExtra("noseat")
        txt_datetime.text = timedate
        txt_day.text = day
        txt_noofseat.text = noseat + " " + "Seats"
        txt_view_day.text = day
        txt_view_day_2.text = day
        txtrestroname.text = restoData!!.rest_name
        txt_time.text = restoData!!.endtime
        txt_totalkm.text = restoData!!.distance + " km"
        booking_id = restoData!!.id
        img_searchicon.setOnClickListener {
            val str_search: String = binding.etvSearchview.text.toString()
            val all_Food_fragment = Select_Food_Fragment()
            if (Utils.isNetworkAvailable(ctx)) {
                all_Food_fragment.callApi_searchfooditem(category_id, str_search)
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
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
                    Hidekeyboard()
                    if (Utils.isNetworkAvailable(ctx)) {
                        val all_Food_fragment = Select_Food_Fragment()
                        all_Food_fragment.callApi_fooditem(category_id)
                    } else {
                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
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
            val all_Food_fragment = Select_Food_Fragment()
            all_Food_fragment.cartListingView()
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            public override fun onPageScrolled(
                position: Int,
                positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            public override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageno", "" + position)
                current_tabactive = position
                val foodList_tab: FoodList_Tab = foodListArrayList!!.get(position)
                category_id = foodList_tab.id
                val all_Food_fragment  = Select_Food_Fragment()
                booking_id?.let { all_Food_fragment.callApi_food_1(category_id, it) }
            }
        })
        if (Utils.isNetworkAvailable(ctx)) {
            callapi_tablisting("1")
        } else {
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fill_tablist() {
        TabLayoutMediator(
            (binding.tabLayout),
            (binding.viewPager),
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                for (i in foodListArrayList!!.indices) {
                    val foodList_tab: FoodList_Tab = foodListArrayList!![position]
                    //tab.setText(foodList_tab.getName());
                    tab.customView = getTabView(foodList_tab.name!!.lowercase(Locale.getDefault()))
                }
            }
        ).attach()
    }

    private fun showAlertView() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@SelectFood_Activity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_tablefood_reservealert, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val btn_share_order: Button = dialogView.findViewById(R.id.btn_share_order)
        btn_share_order.setOnClickListener {
            val intent: Intent = Intent(
                this@SelectFood_Activity,
                BookingOrder_ReserverConformationActivity::class.java
            )
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun callapi_tablisting(branch_id: String) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        info.getres_foodlist(branch_id)!!.enqueue(object : Callback<JsonObject?> {
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
                                    val foodList_tab: FoodList_Tab = FoodList_Tab()
                                    val mjson_obj: JSONObject =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    foodList_tab.name = mjson_obj.getString("name")
                                    foodList_tab.id = mjson_obj.getString("id")
                                    foodListArrayList!!.add(foodList_tab)
                                }
                                binding.progressBar.visibility = View.GONE
                                val viewPagerAdapter_reserveSeat: ViewPagerAdapter_SelectFood =
                                    ViewPagerAdapter_SelectFood(
                                        supportFragmentManager,
                                        lifecycle,
                                        foodListArrayList!!,
                                        tableList!!,
                                        restoData!!
                                    )
                                binding.viewPager.adapter = viewPagerAdapter_reserveSeat
                                fill_tablist()
                            }
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                //stopProgress();
            }
        })
    }

    private fun Hidekeyboard() {
        etv_searchview!!.clearFocus()
        val `in`: InputMethodManager = this@SelectFood_Activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        `in`.hideSoftInputFromWindow(etv_searchview!!.windowToken, 0)
    }

    fun getTabView(str: String?): View {
        val tab: View =
            LayoutInflater.from(this@SelectFood_Activity).inflate(R.layout.custom_tab, null)
        val tv: TextView = tab.findViewById(R.id.custom_text)
        tv.text = str
        return tab
    }
}