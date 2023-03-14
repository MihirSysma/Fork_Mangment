package com.forkmang.activity

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
import com.forkmang.helper.Constant.ENTER_MOBILE
import com.forkmang.helper.Constant.ERRORMSG
import com.forkmang.helper.Constant.ERROR_CODE
import com.forkmang.helper.Constant.IDENTFIER
import com.forkmang.helper.Constant.KEEP_LOGIN
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.NAME
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Constant.PASSWORD
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.Constant.TOKEN_REG
import com.forkmang.helper.Constant.VALID_NO
import com.forkmang.helper.StorePreference
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
    private val storePreference by lazy { StorePreference(this) }
    var token: String = ""
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val txtForgotPassword: TextView = findViewById(R.id.txtForgotPassword)
        val btnReg: Button = findViewById(R.id.BtnReg)
        val btnLogin: Button = findViewById(R.id.BtnLogin)

        //facebook login code start
        //printHashKey();
        sdkInitialize(applicationContext)
        binding.buttonFacebookRoot.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
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
                        showToastMessage(NETWORKEROORMSG)
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
                    KEEP_LOGIN = true
                } else if (!binding.chekKeeplogin.isChecked) {
                    KEEP_LOGIN = false
                }
            } else {
                showToastMessage(Constant.MOBILE_PASSWORD)
            }
        }
        btnLogin.setOnClickListener {
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
                            showToastMessage(NETWORKEROORMSG)
                        }
                        //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        showToastMessage(PASSWORD)
                    }
                } else {
                    showToastMessage(VALID_NO)
                }
            } else {
                showToastMessage(ENTER_MOBILE)
            }
        }
        txtForgotPassword.setOnClickListener {
            storePreference.setString(
                MOBILE,
                binding.etvMobile.text.toString()
            )
            val mainIntent = Intent(this@LoginFormActivity, ForgotPassword::class.java)
            startActivity(mainIntent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        btnReg.setOnClickListener {
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
        binding.progressBar.visibility = View.VISIBLE
        Api.info.loginUser(contact, password)?.enqueue(object : Callback<JsonObject?> {
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
                            storePreference.setBoolean("keeplogin", KEEP_LOGIN)
                            storePreference.setString(
                                MOBILE,
                                jsonObject.getJSONObject("data").getString("contact")
                            )
                            storePreference.setString(
                                NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                            storePreference.setString(
                                TOKEN_LOGIN,
                                jsonObject.getJSONObject("data").getString("token")
                            )
                            storePreference.setString(IDENTFIER, "")

                            /*if(binding.chekKeeplogin.isChecked())
                                    {
                                        storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                    }*/

                            binding.progressBar.visibility = View.GONE
                            val mainIntent = Intent(ctx, DashBoardActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(ERRORMSG)
                        }
                    } else if (response.code() == ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        if (jsonObject.getString("status")
                                .equals(ERROR_CODE.toString(), ignoreCase = true)
                        ) {
                            binding.progressBar.visibility = View.GONE
                            val errorMsg: String = jsonObject.getString("message")
                            showToastMessage(errorMsg)
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(ERRORMSG)
                        }
                    }
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(ERRORMSG)
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun callApiSocialLogin(token: String, userid: String, type: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.registerSocialLogin(type, userid)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(response.body()))
                    Log.d("Result", jsonObject.toString())
                    if (jsonObject.getString("status").equals("Success", ignoreCase = true)) {
                        showToastMessage(jsonObject.getString("message"))
                        storePreference.setString(
                            TOKEN_REG,
                            jsonObject.getJSONObject("data").getString("token")
                        )
                        if (jsonObject.getJSONObject("data").has("name")) {
                            storePreference.setString(
                                NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                        } else {
                            storePreference.setString(NAME, type)
                        }
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(ctx, DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToastMessage(ERRORMSG)
                    }
                    binding.progressBar.visibility = View.GONE
                } catch (ex: JSONException) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    showToastMessage(ERRORMSG)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                showToastMessage(ERRORMSG)
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