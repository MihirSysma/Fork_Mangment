package com.forkmang.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.forkmang.R
import com.forkmang.databinding.ActivityRegisterBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class RegisterActivity : AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener {

    private val callbackManager by lazy { create() }
    var ctx: Context = this@RegisterActivity
    private val storePrefrence by lazy { StorePrefrence(this) }
    var name: String? = null
    var mobile: String? = null
    var password: String? = null
    var confpassword: String? = null
    var email: String? = null
    private var googleApiClient: GoogleApiClient? = null

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val Btn_Back: Button = findViewById(R.id.Btn_Back)
        binding.etvMobile.setText("9836608967")
        binding.etvUsername.setText("967 name")
        binding.etvEmail.setText("test967@gmail.com")
        binding.etvPassword.setText("123456")
        binding.etvCnfpassword.setText("123456")

        printHashKey()
        sdkInitialize(applicationContext)
        binding.buttonFacebookRoot.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("res", "cancle")
                }

                override fun onError(error: FacebookException) {
                    Log.d("res", "error")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("id", result.accessToken.userId)
                    Log.d("token", result.accessToken.token)
                    callApiSocialLogin(
                        result.accessToken.token,
                        result.accessToken.userId,
                        "facebook"
                    )
                }

            }
        )
        //facebook login code end

        //google login code start
        googleIntialization()
        // google login code end
        binding.btnRegister.setOnClickListener {
            //validation
            name = binding.etvUsername.text.toString()
            mobile = binding.etvMobile.text.toString()
            password = binding.etvPassword.text.toString()
            confpassword = binding.etvCnfpassword.text.toString()
            email = binding.etvEmail.text.toString()
            if (binding.etvUsername.text?.isNotEmpty() == true) {
                if (mobile?.isNotEmpty() == true) {
                    if (mobile?.length == 10) {
                        //Email is empty or not
                        if (Objects.requireNonNull<Editable?>(binding.etvEmail.text).isNotEmpty()) {
                            //Email is valid or not
                            if (isValidEmail(
                                    binding.etvEmail.text.toString()
                                )
                            ) {
                                //password is valid or not
                                if ((password?.length ?: 0) > 3) {
                                    if ((confpassword?.length ?: 0) > 3) {
                                        if ((password == confpassword)) {
                                            //call api
                                            val phoneNumber: String =
                                                ("+" + "96" + binding.etvMobile.text.toString())
                                            //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                                            try {
                                                binding.progressBar.visibility = View.VISIBLE
                                                // TODO: Do API Call here with all the fields, get success code and then show otp code field, after that use new api verifyotp to complete register
                                                callApiRegisterUser(name, email, phoneNumber, password, confpassword)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                showToastMessage(e.message ?: "null")
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
                            } else {
                                showToastMessage(Constant.VALIDEmail)
                            }
                        } else {
                            showToastMessage(Constant.EmptyEmail)
                        }
                    } else {
                        showToastMessage(Constant.VALID_NO)
                    }
                } else {
                    showToastMessage(Constant.ENTER_MOBILE)
                }
            } else {
                showToastMessage(Constant.ENTER_NAME)
            }
        }
        Btn_Back.setOnClickListener { finish() }

        //facebook login
        binding.buttonFacebook.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                // already logged out
                binding.buttonFacebookRoot.performClick()
            } else {
                showToastMessage("Already Login wait for Logout process")
                disconnectFromFacebook()
            }
        }
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

    fun printHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures") val info: PackageInfo =
                packageManager.getPackageInfo(
                    "com.forkmang",
                    PackageManager.GET_SIGNATURES
                )
            for (signature: Signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(
                    "KeyHash:",
                    Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        } catch (ignored: NoSuchAlgorithmException) {
        }
    }

    private fun showAlertView(verificationId: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.view_otp, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val Btn_Submit: Button = dialogView.findViewById(R.id.btn_submit)
        val firstPinView: PinView = dialogView.findViewById(R.id.firstPinView)
        //firstPinView.setText("123456")
        Btn_Submit.setOnClickListener {
            // TODO: call verify otp API
        }
        dialog.show()
    }

    private fun showAlertView_2() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.otp_conform, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val Btn_Done: Button = dialogView.findViewById(R.id.btn_done)
        Btn_Done.setOnClickListener {
            val mainIntent = Intent(ctx, DashBoardActivity::class.java)
            startActivity(mainIntent)
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun callApiRegisterUser(
        name: String?,
        email: String?,
        mobile_no: String,
        password: String?,
        cnf_password: String?
    ) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_user(name, email, mobile_no, password, cnf_password)
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
                                showToastMessage(jsonObject.getString("message"))
                                //TODO: get new details from API
                                //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));
                                storePrefrence.setString(
                                    Constant.NAME,
                                    jsonObject.getJSONObject("data").getString("name")
                                )
                                storePrefrence.setString(
                                    Constant.MOBILE,
                                    jsonObject.getJSONObject("data").getString("contact")
                                )
                                storePrefrence.setString(
                                    Constant.TOKEN_LOGIN,
                                    jsonObject.getJSONObject("data").getString("token")
                                )
                                storePrefrence.setString(Constant.IDENTFIER, "")
                                showAlertView_2()
                            } else {
                                binding.progressBar.visibility = View.GONE
                                showToastMessage(jsonObject.getString("status"))
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            if (jsonObject.getString("status").equals("422", ignoreCase = true)) {
                                val error_msg: String =
                                    jsonObject.getJSONObject("message").getJSONArray("email")
                                        .getString(0)
                                val error_msg_2: String =
                                    jsonObject.getJSONObject("message").getJSONArray("contact")
                                        .getString(0)
                                showToastMessage(error_msg)
                                showToastMessage(error_msg_2)
                                binding.progressBar.visibility = View.GONE
                            } else {
                                binding.progressBar.visibility = View.GONE
                                showToastMessage("Error occur please try again")
                            }
                        } else {
                            binding.progressBar.visibility = View.GONE
                            showToastMessage(response.code().toString())
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

    private fun callApiSocialLogin(token: String?, userid: String?, type: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_sociallogin(type, userid)?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(response.body()))
                    Log.d("Result", jsonObject.toString())
                    if (jsonObject.getString("status").equals("Success", ignoreCase = true)) {
                        showToastMessage(jsonObject.getString("message"))
                        //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));
                        if (jsonObject.getJSONObject("data").has("name")) {
                            storePrefrence.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                        } else {
                            storePrefrence.setString(Constant.NAME, type)
                        }
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(ctx, LoginFormActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToastMessage("Error occur please try again")
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

    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null, HttpMethod.DELETE,
            callback = {
                LoginManager.getInstance().logOut()
            })
            .executeAsync()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        // add this line
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == RC_SIGN_IN) {
            val result: GoogleSignInResult? =
                Auth.GoogleSignInApi.getSignInResultFromIntent((data)!!)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result?.isSuccess == true) {
            //gotoProfile();
            val account: GoogleSignInAccount? = result.signInAccount
            Log.d("id", account?.id.toString())
            if (Utils.isNetworkAvailable(ctx)) {
                callApiSocialLogin(
                    account?.idToken,
                    account?.id,
                    "google"
                )
            } else {
                showToastMessage(Constant.NETWORKEROORMSG)
            }
        } else {
            showToastMessage("Sign in cancel")
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private const val RC_SIGN_IN: Int = 1
        fun isValidEmail(target: CharSequence?): Boolean {
            return (!TextUtils.isEmpty(target) && target?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true)
        }
    }
}