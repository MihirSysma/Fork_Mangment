package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.R
import com.forkmang.adapter.ADD_QTY
import com.forkmang.adapter.CartListingAdapterSummary
import com.forkmang.adapter.REMOVE_CART_ITEM
import com.forkmang.data.*
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityPaymentViewBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPaymentSummary : AppCompatActivity() {

    var tableList_get: TableList? = null
    var restoData: RestoData? = null
    private val storePrefrence by lazy { StorePrefrence(this) }
    var ctx: Context = this@ActivityPaymentSummary
    var cartBookingArrayList: ArrayList<CartBooking>? = null
    var coming_from: String? = null

    private val binding by lazy { ActivityPaymentViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //ArrayList<CartBooking> cartBookingArrayList  = extras.getParcelableArrayList("cartbookingarraylist");
        coming_from = intent.getStringExtra("comingfrom")
        if (coming_from.equals("SelectFood", ignoreCase = true)) {
            tableList_get = intent.getSerializableExtra("model") as TableList?
            binding.txtHotelname.text = tableList_get?.str_hotel_name
            binding.txtCustomername.text = tableList_get?.str_customer_name
            binding.txtNoofseat.text = tableList_get?.number_of_person + " Seats"
            binding.txtDateTime.text = tableList_get?.str_time
        } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
            restoData = intent.getSerializableExtra("restromodel") as RestoData?
            binding.txtHotelname.text = restoData?.rest_name
            binding.txtCustomername.text = storePrefrence.getString(Constant.NAME)
            binding.linear1.visibility = View.GONE
            binding.linearViewLayout2.visibility = View.GONE
        }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        binding.txtPhoneno.text = storePrefrence.getString(Constant.MOBILE)
        binding.progressBar.visibility = View.VISIBLE
        binding.recycleview.layoutManager = LinearLayoutManager(this@ActivityPaymentSummary)

        if (loadLocale().equals("ar", ignoreCase = true)) {
            binding.lytEng.visibility = View.GONE
            binding.lytArabic.visibility = View.VISIBLE
        } else {
            //english
            binding.lytArabic.visibility = View.GONE
            binding.lytEng.visibility = View.VISIBLE
        }
        binding.btnPaymentProceed.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                if (coming_from.equals("SelectFood", ignoreCase = true)) {
                    callApiCreateOrder()
                } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
                    callApiCreateOrderPickup()
                }
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        binding.progressBar.visibility = View.GONE
        if (Utils.isNetworkAvailable(ctx)) {
            callApiCartListView()
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    fun callApiCartListView() {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.getcart_detail(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                val data_obj = obj.getJSONObject("data")
                                val cart_array_item = data_obj.getJSONArray("cart_item")
                                cartBookingArrayList = ArrayList()
                                for (i in 0 until cart_array_item.length()) {
                                    val cartBooking = CartBooking()
                                    val cart_detail_obj = cart_array_item.getJSONObject(i)


                                    //data obj
                                    cartBooking.data_userid = data_obj.getString("user_id")
                                    if (data_obj.has("booking_table_id")) {
                                        cartBooking.data_booking_table_id =
                                            data_obj.getString("booking_table_id")
                                    } else {
                                        cartBooking.data_booking_table_id = ""
                                    }
                                    cartBooking.data_total = data_obj.getString("total")

                                    //cart_item obj
                                    cartBooking.cart_item_qty = cart_detail_obj.getString("qty")
                                    cartBooking.cart_item_cartid =
                                        cart_detail_obj.getString("cart_id")
                                    cartBooking.cart_item_id = cart_detail_obj.getString("id")
                                    cartBooking.cart_item_extra_id =
                                        cart_detail_obj.getString("item_extra_id")

                                    //cart_item_details obj
                                    cartBooking.cart_item_details_category_id =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("category_id")
                                    cartBooking.cart_item_details_name =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("name")
                                    cartBooking.cart_item_details_price =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("price")


                                    //extra_item_details obj
                                    val extra_namelist = ArrayList<String>()
                                    val extra_pricelist = ArrayList<String>()
                                    for (j in 0 until cart_detail_obj.getJSONArray("extra_item_details")
                                        .length()) {
                                        val extra_item_obj =
                                            cart_detail_obj.getJSONArray("extra_item_details")
                                                .getJSONObject(j)
                                        extra_namelist.add(extra_item_obj.getString("name"))
                                        extra_pricelist.add(extra_item_obj.getString("price"))


                                        //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                    }

                                    //extra name
                                    var str_extraname = ""
                                    for (k in extra_namelist.indices) {
                                        str_extraname = if (k == 0) {
                                            extra_namelist[k]
                                        } else {
                                            str_extraname + "," + extra_namelist[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_name = str_extraname

                                    //extra price
                                    var str_extraprice = ""
                                    for (k in extra_pricelist.indices) {
                                        str_extraprice = if (k == 0) {
                                            extra_pricelist[k]
                                        } else {
                                            str_extraprice + "," + extra_pricelist[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_price = str_extraprice
                                    cartBookingArrayList?.add(cartBooking)
                                }
                                binding.progressBar.visibility = View.GONE
                                val data_total = cartBookingArrayList?.get(0)?.data_total
                                binding.txtTotalPay.text = ctx.resources.getString(R.string.rupee) + data_total
                                //call adapter
                                val cartListingAdapterSummary =
                                    CartListingAdapterSummary(
                                        ctx,
                                        cartBookingArrayList
                                    ) { func_name, cart_item_id, qty_update ->
                                        when (func_name) {
                                            ADD_QTY -> {
                                                callApiAddQty(cart_item_id, qty_update)
                                            }
                                            REMOVE_CART_ITEM -> {
                                                callApiRemoveItemcart(cart_item_id)
                                            }
                                        }
                                    }
                                binding.recycleview.adapter = cartListingAdapterSummary
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            showToastMessage(jsonObject?.getString("message").toString())
                            binding.progressBar.visibility = View.GONE
                            /*JSONObject obj = new JSONObject(loadJSONFromAsset_t());

                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    JSONArray cart_array_item = data_obj.getJSONArray("cart_item");
                                    cartBookingArrayList = new ArrayList<>();
                                    for(int i = 0; i<cart_array_item.length(); i++)
                                    {
                                        CartBooking cartBooking = new CartBooking();
                                        JSONObject cart_detail_obj = cart_array_item.getJSONObject(i);


                                        //data obj
                                        cartBooking.setData_userid(data_obj.getString("user_id"));
                                        cartBooking.setData_booking_table_id(data_obj.getString("booking_table_id"));
                                        cartBooking.setData_total(data_obj.getString("total"));

                                        //cart_item obj
                                        cartBooking.setCart_item_qty(cart_detail_obj.getString("qty"));
                                        cartBooking.setCart_item_cartid(cart_detail_obj.getString("cart_id"));
                                        cartBooking.setCart_item_id(cart_detail_obj.getString("item_id"));
                                        cartBooking.setCart_item_extra_id(cart_detail_obj.getString("item_extra_id"));

                                        //cart_item_details obj
                                        cartBooking.setCart_item_details_category_id(cart_detail_obj.getJSONObject("cart_item_details").getString("category_id"));
                                        cartBooking.setCart_item_details_name(cart_detail_obj.getJSONObject("cart_item_details").getString("name"));
                                        cartBooking.setCart_item_details_price(cart_detail_obj.getJSONObject("cart_item_details").getString("price"));


                                        //extra_item_details obj
                                        ArrayList<String>extra_namelist = new ArrayList<>();
                                        ArrayList<String>extra_pricelist = new ArrayList<>();
                                        for(int j = 0; j<cart_detail_obj.getJSONArray("extra_item_details").length(); j++)
                                        {
                                            JSONObject extra_item_obj = cart_detail_obj.getJSONArray("extra_item_details").getJSONObject(j);

                                            extra_namelist.add(extra_item_obj.getString("name"));
                                            extra_pricelist.add(extra_item_obj.getString("price"));


                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }

                                        //extra name
                                        String str_extraname ="";
                                        for(int k =0; k<extra_namelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraname = extra_namelist.get(k); }
                                            else{  str_extraname = str_extraname+","+extra_namelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_name(str_extraname);

                                        //extra price
                                        String str_extraprice ="";
                                        for(int k =0; k<extra_pricelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraprice = extra_pricelist.get(k); }
                                            else{  str_extraprice = str_extraprice+","+extra_pricelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_price(str_extraprice);
                                        cartBookingArrayList.add(cartBooking);

                                   }
                                    progressBar.setVisibility(View.GONE);
                                    //call adapter
                                    CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(getActivity(),cartBookingArrayList);
                                    recycleView.setAdapter(cartBookingAdapter);

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                    //stopProgress();
                }
            })
    }

    fun callApiAddQty(cart_itemid: String?, qty: String?) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.cart_updateqty(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            cart_itemid,
            qty,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                showToastMessage(obj.getString("message"))
                                binding.progressBar.visibility = View.GONE
                                if (Utils.isNetworkAvailable(ctx)) {
                                    callApiCartListView()
                                } else {
                                    showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                }
            })
    }

    fun callApiRemoveItemcart(cart_itemid: String?) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.cart_removeqty(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            cart_itemid,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                showToastMessage(obj.getString("message"))
                                binding.progressBar.visibility = View.GONE
                                if (Utils.isNetworkAvailable(ctx)) {
                                    callApiCartListView()
                                } else {
                                    showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                }
            })
    }

    private fun callApiCreateOrder() {
        Api.info.create_order(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            restoData?.id,
            "book_table",
            "",
            storePrefrence.getString(Constant.BOOKINGID),
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val order_id = jsonObject.getString("data")

                                //Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                val dialog = showAlertViewConfirmTable()
                                Handler().postDelayed({
                                    dialog.dismiss()
                                    val mainIntent = Intent(
                                        this@ActivityPaymentSummary,
                                        PaymentScreenActivity::class.java
                                    )
                                    if (coming_from.equals("SelectFood", ignoreCase = true)) {
                                        mainIntent.putExtra("model", tableList_get)
                                    } else if (coming_from.equals(
                                            "PickupFood",
                                            ignoreCase = true
                                        )
                                    ) {
                                        //nothing to send table object
                                    }
                                    mainIntent.putExtra("comingfrom", coming_from)
                                    mainIntent.putExtra("restromodel", restoData)
                                    mainIntent.putExtra(
                                        "totalpay",
                                        binding.txtTotalPay.text.toString()
                                    )
                                    mainIntent.putExtra("orderid", order_id)
                                    mainIntent.putExtra("isbooktable", "no")
                                    startActivity(mainIntent)
                                }, 1000)
                            } else {
                                showToastMessage(jsonObject.getString("message"))
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                        } else if (response.code() == Constant.GUESTUSERlOGIN) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                            val intent = Intent(ctx, LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                            //Toast.makeText(ctx,"You are guest user please login",Toast.LENGTH_SHORT).show();
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage("Error occur please try again")
                }
            })
    }

    private fun callApiCreateOrderPickup() {
        Api.info.create_order(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            restoData?.id, "pickup", "", "", storePrefrence.getString(Constant.IDENTFIER)
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val order_id = jsonObject.getString("data")
                                //Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                val dialog = showAlertViewConfirmTable()
                                Handler().postDelayed({
                                    dialog.dismiss()
                                    if (coming_from.equals("PickupFood", ignoreCase = true)) {
                                        val mainIntent = Intent(
                                            this@ActivityPaymentSummary,
                                            PaymentScreenActivity::class.java
                                        )
                                        mainIntent.putExtra("restromodel", restoData)
                                        mainIntent.putExtra("comingfrom", "PickupFood")
                                        mainIntent.putExtra(
                                            "totalpay",
                                            binding.txtTotalPay.text.toString()
                                        )
                                        mainIntent.putExtra("orderid", order_id)
                                        mainIntent.putExtra("isbooktable", "no")
                                        startActivity(mainIntent)
                                    } else {
                                        val mainIntent = Intent(
                                            this@ActivityPaymentSummary,
                                            PaymentScreenActivity::class.java
                                        )
                                        mainIntent.putExtra("model", tableList_get)
                                        mainIntent.putExtra("restromodel", restoData)
                                        mainIntent.putExtra("comingfrom", "SelectFood")
                                        mainIntent.putExtra(
                                            "totalpay",
                                            binding.txtTotalPay.text.toString()
                                        )
                                        mainIntent.putExtra("orderid", order_id)
                                        mainIntent.putExtra("isbooktable", "no")
                                        startActivity(mainIntent)
                                    }
                                }, 1000)
                            } else {
                                showToastMessage(jsonObject.getString("message"))
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                        } else if (response.code() == Constant.GUESTUSERlOGIN) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            showToastMessage(jsonObject.getString("message"))
                            val intent = Intent(ctx, LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                            //Toast.makeText(ctx,"You are guest user please login",Toast.LENGTH_SHORT).show();
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    showToastMessage("Error occur please try again")
                }
            })
    }

    private fun loadLocale(): String? {
        val langPref = "Language"
        val prefs = getSharedPreferences(
            "CommonPrefs",
            MODE_PRIVATE
        )
        return prefs.getString(langPref, "")
    }

    private fun showAlertViewConfirmTable(): AlertDialog {
        val alertDialog = AlertDialog.Builder(this@ActivityPaymentSummary)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.conform_tablereserve_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        dialog.show()
        return dialog
    }
}