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
    var tableList_get: TableList? = null
    var RestroData: RestoData? = null
    var totalpay: String? = null
    var ctx: Context = this@PaymentScreenActivity
    private val storePrefrence by lazy { StorePrefrence(this) }
    var order_id: String? = null
    var booking_id: String? = null
    var payment_type: String? = null
    var isbooktable: String? = null
    var order_id_get: String? = null
    var coming_from: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_screen)
        val relative_view_1: RelativeLayout = findViewById(R.id.relative_view_1)
        val relative_view_2: RelativeLayout = findViewById(R.id.relative_view_2)
        val radioButton_cash: RadioButton = findViewById(R.id.radioButton1)
        val radioButton_online: RadioButton = findViewById(R.id.radioButton2)
        val btn_payment: Button = findViewById(R.id.btn_payment)
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
        relative_view_1.setOnClickListener {
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            radioButton_cash.setTextColor(ContextCompat.getColor(this, R.color.white))
            radioButton_online.setTextColor(ContextCompat.getColor(this, R.color.black))
            radioButton_online.isChecked = false
            radioButton_cash.isChecked = true
        }
        relative_view_2.setOnClickListener {
            relative_view_2.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
            relative_view_1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            radioButton_online.setTextColor(ContextCompat.getColor(this, R.color.white))
            radioButton_cash.setTextColor(ContextCompat.getColor(this, R.color.black))
            radioButton_cash.isChecked = false
            radioButton_online.isChecked = true
        }
        btn_payment.text = "Pay - $totalpay"
        btn_payment.setOnClickListener { v: View? ->
            if (radioButton_cash.isChecked) {
                payment_type = "cash"
            } else if (radioButton_online.isChecked) {
                payment_type = "online"
            }
            if (Utils.isNetworkAvailable(ctx)) {
                if (isbooktable.equals("yes", ignoreCase = true)) {
                    //callApi_makepayment_1(order_id, payment_type);
                    callApi_makepayment("", booking_id, payment_type, "table")
                } else {
                    if (coming_from.equals("SelectFood", ignoreCase = true)) {
                        callApi_makepayment(
                            order_id,
                            storePrefrence.getString(Constant.BOOKINGID),
                            payment_type,
                            "order"
                        )
                    } else if (coming_from.equals("PickupFood", ignoreCase = true)) {
                        // not to get table object
                        callApi_makepayment(order_id, "", payment_type, "order")
                    }
                }
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
    }

    fun callApi_makepayment(
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
                                    Order_ConformationActivity::class.java
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