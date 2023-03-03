package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.forkmang.R
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityPaymentScreenBinding
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

class PaymentScreenActivity : AppCompatActivity() {
    var ctx: Context = this@PaymentScreenActivity
    var tableList_get: TableList? = null
    var RestroData: RestoData? = null
    var totalpay: String? = null
    var order_id: String? = null
    var booking_id: String? = null
    var payment_type: String? = null
    var isbooktable: String? = null
    var coming_from: String? = null
    private val storePrefrence by lazy { StorePrefrence(this) }
    private val binding by lazy { ActivityPaymentScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        coming_from = intent.getStringExtra("comingfrom")
        if (coming_from.equals("SelectFood", ignoreCase = true)) {
            tableList_get = intent.getSerializableExtra("model") as TableList?
        } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
            // not to get table object
        }
        RestroData = intent.getSerializableExtra("restromodel") as RestoData?
        totalpay = intent.getStringExtra("totalpay")
        isbooktable = intent.getStringExtra("isbooktable")
        if (isbooktable.equals("yes", ignoreCase = true)) {
            booking_id = intent.getStringExtra("bookingid")
        } else if (isbooktable.equals("no", ignoreCase = true)) {
            order_id = intent.getStringExtra("orderid")
        }
        binding.relativeView1.setOnClickListener {
            binding.relativeView1.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
            binding.relativeView2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.radioButton1.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.radioButton2.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.radioButton2.isChecked = false
            binding.radioButton1.isChecked = true
        }
        binding.relativeView2.setOnClickListener {
            binding.relativeView2.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
            binding.relativeView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.radioButton2.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.radioButton1.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.radioButton1.isChecked = false
            binding.radioButton2.isChecked = true
        }
        binding.btnPayment.text = "Pay - $totalpay"
        binding.btnPayment.setOnClickListener {
            if (binding.radioButton1.isChecked) {
                payment_type = "cash"
            } else if (binding.radioButton2.isChecked) {
                payment_type = "online"
            }
            if (Utils.isNetworkAvailable(ctx)) {
                if (isbooktable.equals("yes", ignoreCase = true)) {
                    //callApi_makepayment_1(order_id, payment_type);
                    callApiMakePayment("", booking_id, payment_type, "table")
                } else {
                    if (coming_from.equals("SelectFood", ignoreCase = true)) {
                        callApiMakePayment(
                            order_id,
                            storePrefrence.getString(Constant.BOOKINGID),
                            payment_type,
                            "order"
                        )
                    } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
                        // not to get table object
                        callApiMakePayment(order_id, "", payment_type, "order")
                    }
                }
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
    }

    private fun callApiMakePayment(
        order_id: String?,
        booking_id: String?,
        payment_type: String?,
        order_type: String?
    ) {
        Api.info.make_payment(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            order_id,
            booking_id,
            payment_type,
            order_type
        )?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                val mainIntent: Intent = Intent(
                                    this@PaymentScreenActivity,
                                    OrderConformationActivity::class.java
                                )
                                if (coming_from.equals("SelectFood", ignoreCase = true)) {
                                    mainIntent.putExtra("model", tableList_get)
                                    mainIntent.putExtra("comingfrom", "SelectFood")
                                } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
                                    mainIntent.putExtra("comingfrom", "PickupFood")
                                }
                                mainIntent.putExtra("restromodel", RestroData)
                                mainIntent.putExtra("totalpay", totalpay)
                                if (isbooktable.equals("yes", ignoreCase = true)) {
                                    mainIntent.putExtra("orderid", booking_id)
                                } else if (isbooktable.equals("no", ignoreCase = true)) {
                                    mainIntent.putExtra("orderid", order_id)
                                }
                                startActivity(mainIntent)
                                finish()
                            } else {
                                showToastMessage(jsonObject.getString("message"))
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject: JSONObject = JSONObject(response.errorBody()?.string())
                            showToastMessage(jsonObject.getString("message"))
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
    } /*public void callApi_makepayment(String order_id,String payment_type)
    {
        Api.getInfo().make_payment("Bearer "+storePrefrence.getString(TOKEN_LOGIN),order_id, payment_type).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE))
                                {
                                    final Intent mainIntent = new Intent(PaymentScreenActivity.this, Order_ConformationActivity.class);
                                    mainIntent.putExtra("model",tableList_get);
                                    mainIntent.putExtra("restromodel", RestroData);
                                    mainIntent.putExtra("totalpay",totalpay);

                                    mainIntent.putExtra("orderid",order_id);
                                    startActivity(mainIntent);
                                }
                                else{
                                    showToastMessage
                                   (ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                showToastMessage
                               (ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();

                            showToastMessage
                           (ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        showToastMessage
                       (ctx, "Error occur please try again", Toast.LENGTH_LONG).show();


                    }
                });
    }*/
}