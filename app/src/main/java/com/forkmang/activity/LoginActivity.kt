package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.databinding.ActivityLoginBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
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

    private val storePrefrence by lazy { StorePrefrence(this) }
    var ctx: Context = this@LoginActivity
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.TextGuest.setOnClickListener {
            if (Utils.isNetworkAvailable(ctx)) {
                callApiLoginAsGuest()
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        binding.BtnReg.setOnClickListener {
            if (storePrefrence.getString(
                    Constant.NAME
                )?.isEmpty() == true
            ) {
                val mainIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(mainIntent)
                //finish();
            } else {
                showToastMessage("User already registered please click login")
            }
        }
        binding.BtnLogin.setOnClickListener {
            val mainIntent = Intent(this@LoginActivity, LoginFormActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun callApiLoginAsGuest() {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        val identifier = "identifier123"
        Api.info.loginGuest(identifier)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {

                    //Log.d("Result", jsonObject.toString());
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            binding.progressBar.visibility = View.GONE
                            storePrefrence.setString(
                                Constant.IDENTFIER,
                                jsonObject.getJSONObject("data").getString("identifier")
                            )
                            storePrefrence.setString(Constant.TOKEN_LOGIN, "")
                            storePrefrence.setString(
                                "id",
                                jsonObject.getJSONObject("data").getString("id")
                            )
                            val mainIntent = Intent(ctx, DashBoardActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage("Error occur please try again")
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        if (jsonObject.getString("status")
                                .equals(Constant.ERROR_CODE.toString(), ignoreCase = true)
                        ) {
                            binding.progressBar.visibility = View.GONE
                            val error_msg: String = jsonObject.getString("message")
                            showToastMessage(error_msg)
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage("Error occur please try again")
                        }
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage("Error occur please try again")
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage("Error occur please try again")
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}