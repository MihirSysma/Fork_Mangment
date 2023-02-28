package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.databinding.ActivityLoginBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.network_call.Api
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    var storePrefrence: StorePrefrence? = null
    var ctx: Context = this@LoginActivity
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storePrefrence = StorePrefrence(ctx)
        binding.TextGuest.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_loginasguest()
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
            }
        }
        binding.BtnReg.setOnClickListener {
            if (storePrefrence!!.getString(
                    Constant.NAME
                )!!.isEmpty()
            ) {
                val mainIntent: Intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(mainIntent)
                //finish();
            } else {
                Toast.makeText(ctx, "User already registered please click login", Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.BtnLogin.setOnClickListener {
            val mainIntent: Intent = Intent(this@LoginActivity, LoginFormActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun callapi_loginasguest() {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        val identifier: String = "identifier123"
        Api.info.login_guest(identifier)!!.enqueue(object : Callback<JsonObject?> {
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
                            binding.progressBar.visibility = View.GONE
                            storePrefrence!!.setString(
                                Constant.IDENTFIER,
                                jsonObject.getJSONObject("data").getString("identifier")
                            )
                            storePrefrence!!.setString(Constant.TOKEN_LOGIN, "")
                            storePrefrence!!.setString(
                                "id",
                                jsonObject.getJSONObject("data").getString("id")
                            )
                            val mainIntent: Intent = Intent(ctx, DashBoard_Activity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                        if (jsonObject.getString("status")
                                .equals(Constant.ERROR_CODE.toString(), ignoreCase = true)
                        ) {
                            binding.progressBar.visibility = View.GONE
                            val error_msg: String = jsonObject.getString("message")
                            Toast.makeText(ctx, error_msg, Toast.LENGTH_SHORT).show()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                                .show()
                        }
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
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}