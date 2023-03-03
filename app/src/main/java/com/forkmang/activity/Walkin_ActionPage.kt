package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R
import com.forkmang.data.RestoData
import com.forkmang.databinding.ActivityWalkinActionBinding
import com.forkmang.helper.Constant
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

class Walkin_ActionPage : AppCompatActivity() {
    var txt_queueno: TextView? = null
    var txt_restoadd: TextView? = null
    var txt_restophone: TextView? = null
    var txt_restroname: TextView? = null
    var progressBar: ProgressBar? = null
    private val storePrefrence by lazy { StorePrefrence(this) }
    var ctx: Context = this@Walkin_ActionPage
    var identifier: String? = null
    var quee_no: String? = null
    var noofperson: String? = null
    var occasion: String? = null
    var area: String? = null
    var restromodel: RestoData? = null

    private val binding by lazy { ActivityWalkinActionBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        txt_queueno = findViewById(R.id.txt_queueno)
        val img_pass: ImageView = findViewById(R.id.img_pass)
        val img_cancel: ImageView = findViewById(R.id.img_cancel)
        val img_exit: ImageView = findViewById(R.id.img_exit)
        progressBar = findViewById(R.id.progressBar)
        txt_restoadd = findViewById(R.id.txt_restoadd)
        txt_restophone = findViewById(R.id.txt_restophone)
        txt_restroname = findViewById(R.id.txt_restroname)
        val intent: Intent = intent
        quee_no = intent.getStringExtra("quee_no")
        noofperson = intent.getStringExtra("person")
        occasion = intent.getStringExtra("occasion")
        area = intent.getStringExtra("area")
        restromodel = getIntent().getSerializableExtra("restromodel") as RestoData?
        binding.txtQueueno.text = "No in current queue: $quee_no"
        binding.txtRestroname.text = restromodel!!.rest_name

        //txt_restophone.setText(storePrefrence.getString(Constant.MOBILE));
        identifier = ""
        img_pass.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_action("accept", identifier!!)
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        img_cancel.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_action("cancel", identifier!!)
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        img_exit.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_action("exit", identifier!!)
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        binding.txtQueueno.setOnClickListener {
            val mainIntent: Intent =
                Intent(this@Walkin_ActionPage, ActivityPaymentSummaryWalkin::class.java)
            startActivity(mainIntent)
        }
    }

    private fun callapi_action(action: String, identifier: String) {
        progressBar?.visibility = View.VISIBLE
        info.queue_action(
            "Bearer " + storePrefrence.getString(Constant.TOKEN_LOGIN),
            action, restromodel!!.id, noofperson, occasion, area, identifier
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        progressBar?.visibility = View.GONE
                        val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                        showToastMessage(jsonObject.getString("message"))
                    } else if (response.code() == Constant.ERROR_CODE_n) {
                        progressBar?.visibility = View.GONE
                        val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    } else {
                        progressBar?.visibility = View.GONE
                        val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                        showToastMessage(jsonObject.getString("message"))
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    progressBar?.visibility = View.GONE
                    //showToastMessage(Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    progressBar?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                progressBar?.visibility = View.GONE
                showToastMessage(Constant.ERRORMSG)
            }
        })
    }
}