package com.forkmang.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.forkmang.R
import com.forkmang.databinding.ActivityLoginFormBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.network_call.Api
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginFormActivity : AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener {
    private val binding by lazy { ActivityLoginFormBinding.inflate(layoutInflater) }
    private var callbackManager: CallbackManager? = null
    var ctx: Context = this@LoginFormActivity
    private var dialog: ProgressDialog? = null
    var storePrefrence: StorePrefrence? = null
    var token: String = ""
    private var googleApiClient: GoogleApiClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        storePrefrence = StorePrefrence(ctx)
        val txtForgotPassword: TextView = findViewById(R.id.txtForgotPassword)
        val BtnReg: Button = findViewById(R.id.BtnReg)
        val BtnLogin: Button = findViewById(R.id.BtnLogin)

        //facebook login code start
        //printHashKey();
        sdkInitialize(applicationContext)
        callbackManager = create()
        binding.buttonFacebookRoot.registerCallback(
            callbackManager!!,
            object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("id", result.accessToken.userId)
                    Log.d("token", result.accessToken.token)
                    if (Utils.isNetworkAvailable(ctx)) {
                        callapi_sociallogin(
                            result.accessToken.token,
                            result.accessToken.userId,
                            "facebook"
                        )
                    } else {
                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancel() {
                    Log.d("res", "cancle")
                }

                override fun onError(e: FacebookException) {
                    Log.d("res", "error")
                }
            })
        //facebook login code end

        //google login code start
        google_intialization()
        // google login code end
        binding.etvMobile.setText("9829020700")
        binding.etvPassword.setText("123456")
        binding.chekKeeplogin.setOnClickListener(View.OnClickListener { v: View? ->
            if (binding.etvMobile.length() == 10 && binding.etvPassword.length() > 3) {
                if (binding.chekKeeplogin.isChecked) {
                    Constant.KEEP_LOGIN = true
                } else if (!binding.chekKeeplogin.isChecked) {
                    Constant.KEEP_LOGIN = false
                }
            } else {
                Toast.makeText(ctx, Constant.MOBILE_PASSWORD, Toast.LENGTH_SHORT).show()
            }
        })
        BtnLogin.setOnClickListener { v: View? ->
            if (binding.etvMobile.length() > 0) {
                if (binding.etvMobile.length() == 10) {
                    //password is valid or not
                    if (binding.etvPassword.length() > 3) {
                        if (Utils.isNetworkAvailable(ctx)) {
                            callapi_loginuser(
                                binding.etvMobile.text.toString(),
                                binding.etvPassword.text.toString()
                            )
                        } else {
                            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
                        }


                        //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, Constant.PASSWORD, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(ctx, Constant.VALID_NO, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(ctx, Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show()
            }
        }
        txtForgotPassword.setOnClickListener { v: View? ->
            val mainIntent: Intent = Intent(this@LoginFormActivity, ForgotPassword::class.java)
            startActivity(mainIntent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        BtnReg.setOnClickListener { v: View? ->
            val mainIntent: Intent = Intent(this@LoginFormActivity, RegisterActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        //facebook login
        binding.buttonFacebook.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                // already logged out
                binding.buttonFacebookRoot.performClick()
            } else {
                // already login logout
                Toast.makeText(ctx, "Already Login wait for logout process", Toast.LENGTH_SHORT)
                    .show()
                disconnectFromFacebook()
            }
        }


        //google login
        binding.signinButtonImg.setOnClickListener {
            val intent: Intent = Auth.GoogleSignInApi.getSignInIntent((googleApiClient)!!)
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun google_intialization() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    private fun callapi_loginuser(contact: String, password: String) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.login_user(contact, password)!!.enqueue(object : Callback<JsonObject?> {
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
                            Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                                .show()
                            storePrefrence!!.setBoolean("keeplogin", Constant.KEEP_LOGIN)
                            storePrefrence!!.setString(
                                Constant.MOBILE,
                                jsonObject.getJSONObject("data").getString("contact")
                            )
                            storePrefrence!!.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                            storePrefrence!!.setString(
                                Constant.TOKEN_LOGIN,
                                jsonObject.getJSONObject("data").getString("token")
                            )
                            storePrefrence!!.setString(Constant.IDENTFIER, "")

                            /*if(binding.chekKeeplogin.isChecked())
                                    {
                                        storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                    }*/

                            //stopProgress();
                            binding.progressBar.visibility = View.GONE
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

    fun showProgress() {
        dialog = ProgressDialog(ctx)
        dialog!!.setCancelable(false)
        dialog!!.setMessage("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun stopProgress() {
        dialog!!.dismiss()
    }

    private fun callapi_sociallogin(token: String, userid: String, type: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_sociallogin(type, userid)!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                    Log.d("Result", jsonObject.toString())
                    if (jsonObject.getString("status").equals("Success", ignoreCase = true)) {
                        Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                            .show()
                        storePrefrence!!.setString(
                            Constant.TOKEN_REG,
                            jsonObject.getJSONObject("data").getString("token")
                        )
                        if (jsonObject.getJSONObject("data").has("name")) {
                            storePrefrence!!.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                        } else {
                            storePrefrence!!.setString(Constant.NAME, type)
                        }
                        binding.progressBar.visibility = View.GONE
                        val intent: Intent = Intent(ctx, DashBoard_Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null, HttpMethod.DELETE,
            callback = {
                GraphRequest.Callback {
                    LoginManager.getInstance().logOut()
                    binding.buttonFacebookRoot.performClick()
                }
            })
            .executeAsync()
    }

    companion object {
        private val RC_SIGN_IN: Int = 1
    }
}