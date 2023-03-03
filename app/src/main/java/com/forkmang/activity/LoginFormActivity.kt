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
import com.forkmang.helper.showToastMessage
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

class LoginFormActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val binding by lazy { ActivityLoginFormBinding.inflate(layoutInflater) }
    private val callbackManager by lazy { create() }
    var ctx: Context = this@LoginFormActivity
    private var dialog: ProgressDialog? = null
    private val storePrefrence by lazy { StorePrefrence(this) }
    var token: String = ""
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val txtForgotPassword: TextView = findViewById(R.id.txtForgotPassword)
        val BtnReg: Button = findViewById(R.id.BtnReg)
        val BtnLogin: Button = findViewById(R.id.BtnLogin)

        //facebook login code start
        //printHashKey();
        sdkInitialize(applicationContext)
        binding.buttonFacebookRoot.registerCallback(
            callbackManager,
            object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("id", result.accessToken.userId)
                    Log.d("token", result.accessToken.token)
                    if (Utils.isNetworkAvailable(ctx)) {
                        callApiSocialLogin(
                            result.accessToken.token,
                            result.accessToken.userId,
                            "facebook"
                        )
                    } else {
                        showToastMessage(Constant.NETWORKEROORMSG)
                    }
                }

                override fun onCancel() {
                    Log.d("res", "cancle")
                }

                override fun onError(error: FacebookException) {
                    Log.d("res", "error")
                }
            })
        //facebook login code end

        //google login code start
        googleIntialization()
        // google login code end
        binding.etvMobile.setText("9829020700")
        binding.etvPassword.setText("123456")
        binding.chekKeeplogin.setOnClickListener {
            if (binding.etvMobile.length() == 10 && binding.etvPassword.length() > 3) {
                if (binding.chekKeeplogin.isChecked) {
                    Constant.KEEP_LOGIN = true
                } else if (!binding.chekKeeplogin.isChecked) {
                    Constant.KEEP_LOGIN = false
                }
            } else {
                showToastMessage(Constant.MOBILE_PASSWORD)
            }
        }
        BtnLogin.setOnClickListener {
            if (binding.etvMobile.length() > 0) {
                if (binding.etvMobile.length() == 10) {
                    //password is valid or not
                    if (binding.etvPassword.length() > 3) {
                        if (Utils.isNetworkAvailable(ctx)) {
                            callApiLoginUser(
                                binding.etvMobile.text.toString(),
                                binding.etvPassword.text.toString()
                            )
                        } else {
                            showToastMessage(Constant.NETWORKEROORMSG)
                        }
                        //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        showToastMessage(Constant.PASSWORD)
                    }
                } else {
                    showToastMessage(Constant.VALID_NO)
                }
            } else {
                showToastMessage(Constant.ENTER_MOBILE)
            }
        }
        txtForgotPassword.setOnClickListener {
            val mainIntent = Intent(this@LoginFormActivity, ForgotPassword::class.java)
            startActivity(mainIntent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        BtnReg.setOnClickListener {
            val mainIntent = Intent(this@LoginFormActivity, RegisterActivity::class.java)
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
                showToastMessage("Already Login wait for logout process")
                disconnectFromFacebook()
            }
        }


        //google login
        binding.signinButtonImg.setOnClickListener {
            val intent: Intent = Auth.GoogleSignInApi.getSignInIntent((googleApiClient)!!)
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun googleIntialization() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    private fun callApiLoginUser(contact: String, password: String) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.login_user(contact, password)?.enqueue(object : Callback<JsonObject?> {
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
                            showToastMessage(jsonObject.getString("message"))
                            storePrefrence.setBoolean("keeplogin", Constant.KEEP_LOGIN)
                            storePrefrence.setString(
                                Constant.MOBILE,
                                jsonObject.getJSONObject("data").getString("contact")
                            )
                            storePrefrence.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                            storePrefrence.setString(
                                Constant.TOKEN_LOGIN,
                                jsonObject.getJSONObject("data").getString("token")
                            )
                            storePrefrence.setString(Constant.IDENTFIER, "")

                            /*if(binding.chekKeeplogin.isChecked())
                                    {
                                        storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                    }*/

                            //stopProgress();
                            binding.progressBar.visibility = View.GONE
                            val mainIntent: Intent = Intent(ctx, DashBoardActivity::class.java)
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
                            showToastMessage( "Error occur please try again")
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
                //stopProgress();
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    fun showProgress() {
        dialog = ProgressDialog(ctx)
        dialog?.setCancelable(false)
        dialog?.setMessage("Please wait...")
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun stopProgress() {
        dialog?.dismiss()
    }

    private fun callApiSocialLogin(token: String, userid: String, type: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_sociallogin(type, userid)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(response.body()))
                    Log.d("Result", jsonObject.toString())
                    if (jsonObject.getString("status").equals("Success", ignoreCase = true)) {
                        showToastMessage(jsonObject.getString("message"))
                        storePrefrence.setString(
                            Constant.TOKEN_REG,
                            jsonObject.getJSONObject("data").getString("token")
                        )
                        if (jsonObject.getJSONObject("data").has("name")) {
                            storePrefrence.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                        } else {
                            storePrefrence.setString(Constant.NAME, type)
                        }
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(ctx, DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToastMessage( "Error occur please try again")
                    }
                    binding.progressBar.visibility = View.GONE
                } catch (ex: JSONException) {
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

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    private fun disconnectFromFacebook() {
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