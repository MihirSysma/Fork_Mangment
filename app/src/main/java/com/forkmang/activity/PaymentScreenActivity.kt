package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.forkmang.R
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityPaymentScreenBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePreference
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.models.TableList
import com.forkmang.network_call.Api
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.moyasar.android.sdk.PaymentConfig
import com.moyasar.android.sdk.PaymentResult
import com.moyasar.android.sdk.PaymentSheet
import com.moyasar.android.sdk.payment.models.Payment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentScreenActivity : AppCompatActivity() {

    var ctx: Context = this@PaymentScreenActivity
    var tableListGet: TableList? = null
    var restroData: RestoData? = null
    var totalpay: Int? = null
    var orderId: String? = null
    var bookingId: String? = null
    var paymentType: String? = null
    var isbooktable: String? = null
    var comingFrom: String? = null
    lateinit var paymentId: String
    lateinit var paymentSheet: PaymentSheet

    private val storePreference by lazy { StorePreference(this) }
    private val binding by lazy { ActivityPaymentScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        comingFrom = intent.getStringExtra("comingfrom")
        if (comingFrom.equals("SelectFood", ignoreCase = true)) {
            tableListGet = intent.getSerializableExtra("model") as TableList?
        } else if (comingFrom.equals("PickupFood", ignoreCase = true)) {
            // not to get table object
        }
        restroData = intent.getSerializableExtra("restromodel") as RestoData?
        totalpay = intent.getIntExtra("totalpay", 0)
        isbooktable = intent.getStringExtra("isbooktable")
        if (isbooktable.equals("yes", ignoreCase = true)) {
            bookingId = intent.getStringExtra("bookingid")
        } else if (isbooktable.equals("no", ignoreCase = true)) {
            orderId = intent.getStringExtra("orderid")
        }
        val config = PaymentConfig(
            amount = totalpay ?: 0,
            currency = "SAR",
            description = "Sample Android SDK Payment",
            apiKey = "pk_test_H3rN5E6jNwTiYunxabgvUSgRUraz8MaQn6ZSVNdj"
        )

        paymentSheet = PaymentSheet(this, { handlePaymentResult(it) }, config)

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
            paymentType = if (binding.radioButton1.isChecked) {
                "cash"
            } else if (binding.radioButton2.isChecked) {
                "online"
            } else {
                showToastMessage("Please select a mode of payment")
                return@setOnClickListener
            }
            if (Utils.isNetworkAvailable(ctx)) {
                if (isbooktable.equals("yes", ignoreCase = true)) {
                    callApiMakePayment("", bookingId, paymentType, "table", totalpay.toString())
                } else {
                    if (comingFrom.equals("SelectFood", ignoreCase = true)) {
                        callApiMakePayment(
                            orderId,
                            storePreference.getString(Constant.BOOKINGID),
                            paymentType,
                            "order",
                            totalpay.toString()
                        )
                    } else if (comingFrom.equals("PickupFood", ignoreCase = true)) {
                        // not to get table object
                        callApiMakePayment(orderId, "", paymentType, "order", totalpay.toString())
                    }
                }

            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
    }

    private fun handlePaymentResult(result: PaymentResult) {
        when (result) {
            is PaymentResult.Completed -> {
                handleCompletedPayment(result.payment);
            }
            is PaymentResult.Failed -> {
                showToastMessage(result.error.message ?: "")
            }
            PaymentResult.Canceled -> {
                // User has canceled the payment
                showToastMessage("Payment canceled")
            }
        }
    }

    private fun handleCompletedPayment(payment: Payment) {
        when (payment.status) {
            "paid" -> {
                callGetPaymentDetails(
                    payment.id,
                    payment.status,
                    payment.amount,
                    payment.currency,
                    payment.invoiceId,
                    payment.source
                )

            }
            "failed" -> {
                val errorMessage = payment.source["message"]
                showToastMessage(errorMessage.toString())
            }
            else -> { /* Handle other statuses */
            }
        }
    }

    private fun callGetPaymentDetails(
        id: String,
        status: String,
        amount: Int,
        currency: String,
        invoiceId: String?,
        source: MutableMap<String, String>
    ) {
        Api.info.getPaymentData(
            "Bearer " + storePreference.getString(Constant.TOKEN_LOGIN),
            id, status, amount.toString(), currency, invoiceId ?: "", source, paymentId

        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            val mainIntent = Intent(
                                this@PaymentScreenActivity,
                                OrderConformationActivity::class.java
                            )
                            if (comingFrom.equals("SelectFood", ignoreCase = true)) {
                                mainIntent.putExtra("model", tableListGet)
                                mainIntent.putExtra("comingfrom", "SelectFood")
                            } else if (comingFrom.equals("PickupFood", ignoreCase = true)) {
                                mainIntent.putExtra("comingfrom", "PickupFood")
                            }
                            mainIntent.putExtra("restromodel", restroData)
                            mainIntent.putExtra("totalpay", totalpay)
                            if (isbooktable.equals("yes", ignoreCase = true)) {
                                mainIntent.putExtra("orderid", bookingId)
                            } else if (isbooktable.equals("no", ignoreCase = true)) {
                                mainIntent.putExtra("orderid", orderId)
                            }
                            startActivity(mainIntent)
                            finish()
                        } else {
                            showToastMessage(jsonObject.getString("message"))
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToastMessage(Constant.ERRORMSG)
                }
            }
            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(Constant.ERRORMSG)
            }

        })
    }

    private fun callApiMakePayment(
        order_id: String?,
        booking_id: String?,
        payment_type: String?,
        order_type: String?,
        amt: String
    ) {
        Api.info.makePayment(
            "Bearer " + storePreference.getString(Constant.TOKEN_LOGIN),
            order_id,
            booking_id,
            payment_type,
            order_type,
            amt
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            paymentId = jsonObject.getJSONObject("data").getString("payment_id")
                            paymentSheet.present()
                        } else {
                            showToastMessage(jsonObject.getString("message"))
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    showToastMessage(Constant.ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(Constant.ERRORMSG)
            }
        })
    }

}