package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityWalkinActionBinding
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.ERROR_CODE_n
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.SUCCESS_CODE_n
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WalkinActionPage : AppCompatActivity() {

    private val storePreference by lazy { StorePrefrence(this) }
    var ctx: Context = this@WalkinActionPage
    var identifier: String? = null
    var queeNo: String? = null
    var noofperson: String? = null
    var occasion: String? = null
    var area: String? = null
    var restromodel: RestoData? = null

    private val binding by lazy { ActivityWalkinActionBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent: Intent = intent
        queeNo = intent.getStringExtra("quee_no")
        noofperson = intent.getStringExtra("person")
        occasion = intent.getStringExtra("occasion")
        area = intent.getStringExtra("area")
        restromodel = getIntent().getSerializableExtra("restromodel") as RestoData?

        binding.txtQueueno.text = "No in current queue: $queeNo"
        binding.txtRestroname.text = restromodel!!.rest_name

        //txt_restophone.setText(storePrefrence.getString(Constant.MOBILE));
        identifier = ""
        binding.imgPass.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callApiAction("accept", identifier!!)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        binding.imgCancel.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callApiAction("cancel", identifier!!)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        binding.imgExit.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callApiAction("exit", identifier!!)
            } else {
                showToastMessage(NETWORKEROORMSG)
            }
        }
        binding.txtQueueno.setOnClickListener {
            val mainIntent =
                Intent(this@WalkinActionPage, ActivityPaymentSummaryWalkin::class.java)
            startActivity(mainIntent)
        }
    }

    private fun callApiAction(action: String, identifier: String) {
        binding.progressBar.visibility = View.VISIBLE
        info.queueAction(
            "Bearer " + storePreference.getString(TOKEN_LOGIN),
            action, restromodel!!.id, noofperson, occasion, area, identifier
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    if (response.code() == SUCCESS_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        showToastMessage(jsonObject.getString("message"))
                    } else if (response.code() == ERROR_CODE_n) {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    } else {
                        binding.progressBar.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    //showToastMessage(Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showToastMessage(ERRORMSG)
            }
        })
    }
}