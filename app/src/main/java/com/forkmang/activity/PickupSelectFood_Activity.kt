package com.forkmang.activity

import android.app.Dialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.forkmang.R
import com.forkmang.adapter.ViewPagerAdapter_PickupSelectFood
import com.forkmang.data.FoodList_Tab
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivitySelectfoodBinding
import com.forkmang.fragment.PickupSelect_Food_Fragment
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
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

class PickupSelectFood_Activity : AppCompatActivity() {
    var viewPager: ViewPager2? = null
    var tabLayout: TabLayout? = null
    var btn_view_cart: Button? = null
    var ctx: Context = this@PickupSelectFood_Activity
    var foodListArrayList: ArrayList<FoodList_Tab>? = null
    var restoData: RestoData? = null
    var tableList: TableList? = null
    var booking_id: String? = null
    var category_id: String? = null
    var current_tabactive: Int = 0

    private val binding by lazy { ActivitySelectfoodBinding.inflate(layoutInflater) }
    private val storePrefrence by lazy { StorePrefrence(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        //tabLayout.setupWithViewPager(viewPager);
        btn_view_cart = findViewById(R.id.btn_view_cart)
        binding.linearView1.visibility = View.GONE
        binding.linearView2.visibility = View.GONE
        binding.linearView3.visibility = View.GONE
        val txt_booktable: TextView = findViewById(R.id.txt_booktable)
        txt_booktable.text = "Pickup Table"
        val img_searchicon: ImageView = findViewById(R.id.img_searchicon)
        val img_edit: ImageView = findViewById(R.id.img_edit)
        val txtrestroname: TextView = findViewById(R.id.txtrestroname)
        val txt_time: TextView = findViewById(R.id.txt_time)
        val txt_totalkm: TextView = findViewById(R.id.txt_totalkm)
        img_edit.setOnClickListener { finish() }
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
        txtrestroname.text = restoData!!.rest_name
        txt_time.text = restoData!!.endtime
        txt_totalkm.text = restoData!!.distance + " km"
        booking_id = restoData!!.id
        img_searchicon.setOnClickListener {
            val str_search: String = binding.etvSearchview.text.toString()
            val all_Food_fragment = PickupSelect_Food_Fragment()
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
                if (s.toString().isEmpty()) {
                    Hidekeyboard()
                    if (Utils.isNetworkAvailable(ctx)) {
                        val all_Food_fragment = PickupSelect_Food_Fragment()
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
            cartListingView()
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("pageno", "" + position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                current_tabactive = position
                val foodList_tab: FoodList_Tab = foodListArrayList!![position]
                category_id = foodList_tab.id
                /*val all_Food_fragment = PickupSelect_Food_Fragment()
                all_Food_fragment.callApi_food_1()*/
            }
        })
        if (Utils.isNetworkAvailable(ctx)) {
            callapi_tablisting("1")
        } else {
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    fun cartListingView() {
        val dialog = Dialog(this, R.style.FullHeightDialog)
        dialog.setContentView(R.layout.cartview_alertview_2)
        if (dialog.window != null) {
            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        val txt_datetime: TextView
        val etv_noperson: EditText
        var linear_view_layout_2: LinearLayout
        val linear_view1: LinearLayout = dialog.findViewById(R.id.linear_view1)
        linear_view1.visibility = View.GONE

        // linear_view_layout_2 =dialog.findViewById(R.id.linear_view_layout_2);
        // linear_view_layout_2.setVisibility(View.GONE);
        val txt_restroname: TextView = dialog.findViewById(R.id.txt_restroname)
        val txt_custname: TextView = dialog.findViewById(R.id.txt_custname)
        val txt_phoneno: TextView = dialog.findViewById(R.id.txt_phoneno)
        val btn_pay_table_food: Button = dialog.findViewById(R.id.btn_pay_table_food)
        val btn_pay_table: Button = dialog.findViewById(R.id.btn_pay_table)
        val img_close: ImageView = dialog.findViewById(R.id.img_close)
        val recycler: RecyclerView = dialog.findViewById(R.id.recycleview)
        txt_restroname.text = PickupSelect_Food_Fragment.restoData?.rest_name ?: ""
        txt_custname.text = storePrefrence.getString(Constant.NAME)
        txt_phoneno.text = storePrefrence.getString(Constant.MOBILE)
        //txt_datetime.setText(tableList_get.getStr_time());
        img_close.setOnClickListener { dialog.dismiss() }
        if (Utils.isNetworkAvailable(this)) {
            //callApi_cartListview()
        } else {
            Toast.makeText(this, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
        btn_pay_table_food.setOnClickListener {
            dialog.dismiss()
            val mainIntent = Intent(this, Activity_PaymentSummary::class.java)
            //Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("cartbookingarraylist", cartBookingArrayList);
            mainIntent.putExtra("comingfrom", "PickupFood")
            mainIntent.putExtra("restromodel", PickupSelect_Food_Fragment.restoData)
            startActivity(mainIntent)
        }
        btn_pay_table.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun fill_tablist() {
        TabLayoutMediator(
            (tabLayout)!!,
            (viewPager)!!
        ) { tab: TabLayout.Tab, position: Int ->
            for (i in foodListArrayList!!.indices) {
                val foodList_tab: FoodList_Tab = foodListArrayList!![position]
                //tab.setText(foodList_tab.getName().toLowerCase());
                tab.customView = getTabView(foodList_tab.name!!.lowercase(Locale.getDefault()))
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
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@PickupSelectFood_Activity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_tablefood_reservealert, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val btn_share_order: Button = dialogView.findViewById(R.id.btn_share_order)
        btn_share_order.setOnClickListener {
            val intent: Intent = Intent(
                this@PickupSelectFood_Activity,
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
                                    val foodList_tab = FoodList_Tab()
                                    val mjson_obj: JSONObject =
                                        jsonObject.getJSONObject("data").getJSONArray("data")
                                            .getJSONObject(i)
                                    foodList_tab.name = mjson_obj.getString("name")
                                    foodList_tab.id = mjson_obj.getString("id")
                                    foodListArrayList!!.add(foodList_tab)
                                }
                                binding.progressBar.visibility = View.GONE
                                val viewPagerAdapter_reserveSeat =
                                    ViewPagerAdapter_PickupSelectFood(
                                        supportFragmentManager,
                                        lifecycle,
                                        foodListArrayList!!,
                                        tableList,
                                        restoData!!
                                    )
                                viewPager!!.adapter = viewPagerAdapter_reserveSeat
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
        binding.etvSearchview.clearFocus()
        val `in`: InputMethodManager = this@PickupSelectFood_Activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.etvSearchview.windowToken, 0)
    }

    fun getTabView(str: String?): View {
        val tab: View =
            LayoutInflater.from(this@PickupSelectFood_Activity).inflate(R.layout.custom_tab, null)
        val tv: TextView = tab.findViewById(R.id.custom_text)
        tv.text = str
        return tab
    }
}