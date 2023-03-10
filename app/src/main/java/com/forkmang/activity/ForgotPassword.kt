package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R
import com.forkmang.databinding.ActivityForgotPasswordBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ForgotPassword : AppCompatActivity() {

    private val binding by lazy { ActivityForgotPasswordBinding.inflate(layoutInflater) }
    var ctx: Context = this@ForgotPassword
    private val storePrefrence by lazy { StorePrefrence(this) }
    var idToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        /*binding.etvNewpas.setText("1234567")
        binding.etvcnfPass.setText("1234567")*/

        binding.btnReset.setOnClickListener {
            /* final Intent mainIntent = new Intent(ForgotPassword.this, FaceLoginPermission.class);
            startActivity(mainIntent);
            finish();*/
            if (binding.etvNewpas.text?.isNotEmpty() == true) {
                if (binding.etvcnfPass.text?.isNotEmpty() == true) {
                    if ((binding.etvNewpas.text.toString() == binding.etvcnfPass.text.toString())) {
                        //call api
                        val contact: String? = storePrefrence.getString(Constant.MOBILE)
                        if (contact != null) {
                            getToken(contact)
                        }
                    } else {
                        showToastMessage(Constant.PASSWORD_MATCH)
                    }
                } else {
                    showToastMessage(Constant.CNFPASSWORD_MATCH)
                }
            } else {
                showToastMessage(Constant.PASSWORD)
            }
        }
    }

    private fun callApiForgetPasswordValid(contact: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.forgotPass(contact, idToken)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        //Log.d("Result", jsonObject.toString());
                        if (jsonObject.getString("status")
                                .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                        ) {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(jsonObject.getString("message"))
                            storePrefrence.setString(
                                Constant.MOBILE,
                                jsonObject.getJSONObject("data").getString("contact")
                            )
                            //storePrefrence.setString(Constant.TOKEN_FORGOTPASS, jsonObject.getJSONObject("data").getString("token"));
                            storePrefrence.setBoolean("keeplogin", false)
                            if (Utils.isNetworkAvailable(ctx)) {
                                callApiResetPassword(
                                    jsonObject.getJSONObject("data").getString("contact"),
                                    binding.etvNewpas.text.toString(),
                                    binding.etvcnfPass.text.toString(),
                                    jsonObject.getJSONObject("data").getString("token")
                                )
                            } else {
                                showToastMessage(Constant.NETWORKEROORMSG)
                            }
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
                            val errorMsg: String = jsonObject.getString("message")
                            showToastMessage(errorMsg)
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

    private fun callApiResetPassword(
        contact: String,
        password: String,
        cnf_password: String,
        token: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.resetPass(contact, password, cnf_password, token)
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>
                ) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE, ignoreCase = true)
                            ) {
                                binding.progressBar.visibility = View.GONE
                                val mainIntent =
                                    Intent(this@ForgotPassword, LoginFormActivity::class.java)
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
                                val errorMsg: String = jsonObject.getString("message")
                                showToastMessage(errorMsg)
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

    private fun getToken(contact: String) {
        //progressBar.setVisibility(View.VISIBLE);
        val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    idToken = task.result.token
                    Log.d("token", (idToken) ?: "null")
                    //progressBar.setVisibility(View.GONE);
                    if (Utils.isNetworkAvailable(ctx)) {
                        callApiForgetPasswordValid(contact)
                    } else {
                        showToastMessage(Constant.NETWORKEROORMSG)
                    }

                    // Send token to your backend via HTTPS
                    // ...
                } else {
                    // Handle error -> task.getException();
                    //progressBar.setVisibility(View.GONE);
                }
            }
    }
}