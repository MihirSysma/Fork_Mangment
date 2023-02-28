package com.forkmang.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.RestoData
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Order_ConformationActivity : AppCompatActivity() {
    var tableList_get: TableList? = null
    var restoData: RestoData? = null
    var recyclerView: RecyclerView? = null
    var txt_totalPaidamt: TextView? = null
    var txt_rule: TextView? = null
    var txt_dresscode: TextView? = null
    var txt_ocassion: TextView? = null
    var txt_customer_add: TextView? = null
    var txt_timeview: TextView? = null
    var txtrestroname: TextView? = null
    var txt_endtime: TextView? = null
    var txt_distance: TextView? = null
    var txttotalkm: TextView? = null
    var ctx: Context = this@Order_ConformationActivity
    var storePrefrence: StorePrefrence? = null
    var totalpay: String? = null
    var order_id: String? = null
    var comingfrom: String? = null
    var progressbar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conform_seatreserve)
        storePrefrence = StorePrefrence(ctx)
        progressbar = findViewById(R.id.progressbar)
        val txt_order_id:TextView = findViewById(R.id.txt_queueno)
        txt_totalPaidamt = findViewById(R.id.txt_totalPaidamt)
        txtrestroname = findViewById(R.id.txt_hotelname)
        txt_endtime = findViewById(R.id.txt_endtime)
        txt_distance = findViewById(R.id.txt_distance)
        txttotalkm = findViewById(R.id.txt_totalkm)
        txt_timeview = findViewById(R.id.txt_timeview)
        val txt_indoor: TextView = findViewById(R.id.txt_indoor)
        val txt_customername: TextView = findViewById(R.id.txt_customername)
        txt_customer_add = findViewById(R.id.txt_customer_add)
        val txt_mobileno : TextView = findViewById(R.id.txt_mobileno)
        txt_rule = findViewById(R.id.txt_rule)
        txt_dresscode = findViewById(R.id.txt_dresscode)
        txt_ocassion = findViewById(R.id.txt_ocassion)
        val linear_view : LinearLayout = findViewById(R.id.linear_view)
        comingfrom = intent.getStringExtra("comingfrom")
        if (comingfrom.equals("SelectFood", ignoreCase = true)) {
            tableList_get = getIntent().getSerializableExtra("model") as TableList?
        } else if (comingfrom.equals("PickupFood", ignoreCase = true)) {
            //not required table object
            linear_view.setVisibility(View.GONE)
        }
        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        totalpay = intent.getStringExtra("totalpay")
        order_id = intent.getStringExtra("orderid")
        txt_order_id.text = "Order Id: " + order_id
        //String data_total = Select_Food_Fragment.cartBookingArrayList.get(0).getData_total();
        if (comingfrom.equals("SelectFood", ignoreCase = true)) {
            txt_customername.text = tableList_get!!.str_customer_name
            txt_indoor.text = tableList_get!!.number_of_person + " " + "Seats"
        } else if (comingfrom.equals("PickupFood", ignoreCase = true)) {
            //not required table object
            txt_customername.text = storePrefrence!!.getString(Constant.NAME)
            txt_indoor.visibility = View.GONE
        }
        txt_mobileno.text = storePrefrence!!.getString(Constant.MOBILE)
        if (Utils.isNetworkAvailable(ctx)) {
            callApi_getOrderDetail(order_id)
        } else {
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    fun callApi_getOrderDetail(order_id: String?) {
        progressbar!!.visibility = View.VISIBLE
        info.get_orderdetail(
            "Bearer " + storePrefrence!!.getString(Constant.TOKEN_LOGIN),
            order_id
        )!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val dataobj: JSONObject = jsonObject.getJSONObject("data")
                                if (dataobj.getJSONArray("booking_table").length() > 0) {
                                    txt_rule!!.text = dataobj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("rules")
                                    txt_dresscode!!.text = dataobj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("dresscode")
                                    //txt_timeview.setText(tableList_get.getStr_time());
                                    txt_timeview!!.text = (dataobj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("date"))
                                    txt_ocassion!!.text = (dataobj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("occasion"))
                                }
                                txtrestroname!!.text = dataobj.getJSONObject("restaurant").getString("rest_name")
                                txt_endtime!!.text = "Branch Name: " + dataobj.getJSONObject("restaurant")
                                    .getString("rest_branch")
                                txt_distance!!.text = "ContactNo: " + dataobj.getJSONObject("restaurant")
                                    .getString("contact")
                                txttotalkm!!.text = restoData!!.distance + " Km"
                                txt_totalPaidamt!!.text = ctx.resources
                                    .getString(R.string.rupee) + dataobj.getString("total")
                            } else {
                                Toast.makeText(
                                    ctx,
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            progressbar!!.visibility = View.GONE
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                            progressbar!!.visibility = View.GONE
                            Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        progressbar!!.visibility = View.GONE
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                    progressbar!!.visibility = View.GONE
                }
            })
    }
}