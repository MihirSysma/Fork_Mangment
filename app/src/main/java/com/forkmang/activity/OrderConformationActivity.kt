package com.forkmang.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityConformSeatreserveBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePreference
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderConformationActivity : AppCompatActivity() {

    var ctx: Context = this@OrderConformationActivity
    var tablelistGet: TableList? = null
    var restoData: RestoData? = null
    var totalpay: String? = null
    var orderId: String? = null
    var comingfrom: String? = null
    private val storePreference by lazy { StorePreference(this) }

    private val binding by lazy { ActivityConformSeatreserveBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        comingfrom = intent.getStringExtra("comingfrom")

        if (comingfrom.equals("SelectFood", ignoreCase = true)) {
            tablelistGet = intent.getSerializableExtra("model") as TableList?
        } else if (comingfrom.equals("PickupFood", ignoreCase = true)) {
            //not required table object
            binding.linearView.visibility = View.GONE
        }

        restoData = intent.getSerializableExtra("restromodel") as RestoData?
        totalpay = intent.getStringExtra("totalpay")
        orderId = intent.getStringExtra("orderid")
        binding.txtQueueno.text = "Order Id: " + orderId
        //String data_total = Select_Food_Fragment.cartBookingArrayList.get(0).getData_total();

        if (comingfrom.equals("SelectFood", ignoreCase = true)) {
            binding.txtCustomername.text = tablelistGet?.str_customer_name
            binding.txtIndoor.text = tablelistGet?.number_of_person + " " + "Seats"
        } else if (comingfrom.equals("PickupFood", ignoreCase = true)) {
            //not required table object
            binding.txtCustomername.text = storePreference.getString(Constant.NAME)
            binding.txtIndoor.visibility = View.GONE
        }

        binding.txtMobileno.text = storePreference.getString(Constant.MOBILE)
        if (Utils.isNetworkAvailable(ctx)) {
            callApiGetOrderDetail(orderId)
        } else {
            showToastMessage(Constant.NETWORKEROORMSG)
        }
    }

    private fun callApiGetOrderDetail(order_id: String?) {
        binding.progressbar.visibility = View.VISIBLE
        info.getOrderDetail(
            "Bearer " + storePreference.getString(Constant.TOKEN_LOGIN),
            order_id
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    //Log.d("Result", jsonObject.toString());
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            val dataObj: JSONObject = jsonObject.getJSONObject("data")
                            if (dataObj.getJSONArray("booking_table").length() > 0) {
                                binding.txtRule.text =
                                    dataObj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("rules")
                                binding.txtDresscode.text =
                                    dataObj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("dresscode")
                                //txt_timeview.setText(tableList_get.getStr_time());
                                binding.txtTimeview.text =
                                    (dataObj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("date"))
                                binding.txtOcassion.text =
                                    (dataObj.getJSONArray("booking_table").getJSONObject(0)
                                        .getString("occasion"))
                            }
                            binding.txtHotelname.text =
                                dataObj.getJSONObject("restaurant").getString("rest_name")
                            binding.txtEndtime.text =
                                "Branch Name: " + dataObj.getJSONObject("restaurant")
                                    .getString("rest_branch")
                            binding.txtDistance.text =
                                "ContactNo: " + dataObj.getJSONObject("restaurant")
                                    .getString("contact")
                            binding.txtTotalkm.text = restoData?.distance + " Km"
                            binding.txtTotalPaidamt.text = ctx.resources
                                .getString(R.string.rupee) + dataObj.getString("total")
                        } else {
                            showToastMessage(jsonObject.getString("message"))
                        }
                        binding.progressbar.visibility = View.GONE
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        binding.progressbar.visibility = View.GONE
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    binding.progressbar.visibility = View.GONE
                    showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(Constant.ERRORMSG)
                binding.progressbar.visibility = View.GONE
            }
        })
    }
}