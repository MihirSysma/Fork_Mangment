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
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.facebook.*
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.forkmang.R
import com.forkmang.databinding.ActivityRegisterBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils
import com.forkmang.network_call.Api
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
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
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener {
    private var callbackManager: CallbackManager? = null
    var mAuth: FirebaseAuth? = null
    var mCallback: OnVerificationStateChangedCallbacks? = null
    var btn_register: Button? = null
    var button_facebook: ImageView? = null
    var signin_button_img: ImageView? = null
    var ctx: Context = this@RegisterActivity
    var storePrefrence: StorePrefrence? = null
    var name: String? = null
    var mobile: String? = null
    var password: String? = null
    var cnfpassword: String? = null
    var email: String? = null
    var idToken: String? = null
    private var googleApiClient: GoogleApiClient? = null
    private var mButtonFacebook: LoginButton? = null
    private var signInButton: SignInButton? = null

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mButtonFacebook = findViewById(R.id.button_facebook_root)
        signInButton = findViewById(R.id.sign_in_button_root)
        button_facebook = findViewById(R.id.button_facebook)
        signin_button_img = findViewById(R.id.signin_button_img)
        val Btn_Back: Button = findViewById(R.id.Btn_Back)
        btn_register = findViewById(R.id.btn_register)
        storePrefrence = StorePrefrence(ctx)
        binding.etvMobile.setText("9836608967")
        binding.etvUsername.setText("967 name")
        binding.etvEmail.setText("test967@gmail.com")
        binding.etvPassword.setText("123456")
        binding.etvCnfpassword.setText("123456")


        //firebase login code
        mAuth = FirebaseAuth.getInstance()
        StartFirebaseLogin()
        //firebase login code end


        //facebook login code start
        printHashKey()
        sdkInitialize(applicationContext)
        callbackManager = create()
        binding.buttonFacebookRoot.registerCallback(
            callbackManager!!,
            object : FacebookCallback<LoginResult>{
                override fun onCancel() {
                    Log.d("res", "cancle")
                }

                override fun onError(error: FacebookException) {
                    Log.d("res", "error")
                }

                override fun onSuccess(result: LoginResult) {
                    Log.d("id", result.accessToken.userId)
                    Log.d("token", result.accessToken.token)
                    callapi_sociallogin(
                        result.accessToken.token,
                        result.accessToken.userId,
                        "facebook"
                    )
                }

            }
        )
        //facebook login code end

        //google login code start
        google_intialization()
        // google login code end
        binding.btnRegister.setOnClickListener{
            //validation
            name = Objects.requireNonNull(binding.etvUsername.text).toString()
            mobile = Objects.requireNonNull(binding.etvMobile.text).toString()
            password = Objects.requireNonNull(binding.etvPassword.text).toString()
            cnfpassword = Objects.requireNonNull(binding.etvCnfpassword.text).toString()
            email = Objects.requireNonNull(binding.etvEmail.text).toString()
            if (binding.etvUsername.text!!.isNotEmpty()) {
                if (mobile!!.isNotEmpty()) {
                    if (mobile!!.length == 10) {
                        //Email is empty or not
                        if (Objects.requireNonNull<Editable?>(binding.etvEmail.text).isNotEmpty()) {
                            //Email is valid or not
                            if (isValidEmail(
                                    binding.etvEmail.text.toString()
                                )
                            ) {
                                //password is valid or not
                                if (password!!.length > 3) {
                                    if (cnfpassword!!.length > 3) {
                                        if ((password == cnfpassword)) {
                                            //call api
                                            val phoneNumber: String =
                                                ("+" + "91" + binding.etvMobile.text.toString())
                                            //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                                            sentRequest(phoneNumber)
                                        } else {
                                            Toast.makeText(
                                                ctx,
                                                Constant.PASSWORD_MATCH,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            ctx,
                                            Constant.CNFPASSWORD_MATCH,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(ctx, Constant.PASSWORD, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                Toast.makeText(ctx, Constant.VALIDEmail, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(ctx, Constant.EmptyEmail, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(ctx, Constant.VALID_NO, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(ctx, Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(ctx, Constant.ENTER_NAME, Toast.LENGTH_SHORT).show()
            }
        }
        Btn_Back.setOnClickListener { finish() }

        //facebook login
        binding.buttonFacebook.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                // already logged out
                binding.buttonFacebookRoot.performClick()
            } else {
                Toast.makeText(ctx, "Already Login wait for Logout process", Toast.LENGTH_SHORT)
                    .show()
                disconnectFromFacebook()
            }
        }
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

    private fun showAlertView() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.view_otp, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val Btn_Submit: Button = dialogView.findViewById(R.id.btn_submit)
        val firstPinView: PinView = dialogView.findViewById(R.id.firstPinView)
        firstPinView.setText("123456")
        Btn_Submit.setOnClickListener {
            //call register api
            //String phoneNumber = ("+" + "91" + etv_mobile.getText().toString());
            val phoneNumber: String = Objects.requireNonNull(binding.etvMobile.text).toString()
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_registeruser(name, email, phoneNumber, password, cnfpassword, idToken)
                dialog.dismiss()
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
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
        /*TextView txt_otpenter;
        txt_otpenter=dialogView.findViewById(R.id.txt_otpenter);
        txt_otpenter.setOnClickListener(v -> {
            //showAlertView();
            dialog.dismiss();
        });*/
        val Btn_Done: Button = dialogView.findViewById(R.id.btn_done)
        Btn_Done.setOnClickListener(OnClickListener { v: View? ->
            val intent: Intent = Intent(ctx, LoginFormActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        })
        dialog.show()
    }

    fun sentRequest(phoneNumber: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            (phoneNumber)!!,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this@RegisterActivity,  // Activity (for callback binding)
            (mCallback)!!
        )
    }

    private fun StartFirebaseLogin() {
        mAuth = FirebaseAuth.getInstance()
        mCallback = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                println("====verification complete call  " + phoneAuthCredential.smsCode)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                //setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), "failed");
                println("====failed" + "failed")
            }

            override fun onCodeSent(p0: String, p1: ForceResendingToken) {
                super.onCodeSent((p0), (p1))
                //System.out.println("====token" + forceResendingToken);
                otpVerification(p0, "123456")
            }
        }
    }

    fun otpVerification(firebase_otp: String?, otptext: String) {
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential((firebase_otp)!!, otptext)
        signInWithPhoneAuthCredential(credential, otptext)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, otptext: String) {
        binding.progressBar.visibility = View.VISIBLE
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@RegisterActivity
            ) { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    val message: String = "Success"
                    println("====Success$message")
                    token
                } else {

                    //verification unsuccessful.. display an error message
                    var message: String = "Something is wrong, we will fix it soon..."
                    println("====failed$message")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                        println("====Invalid$message")
                    }
                    //edtotp.setError(message);
                }
            }
    }// Handle error -> task.getException();

    // Send token to your backend via HTTPS
    // ...
    private val token: Unit
        get() {
            val mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            mUser!!.getIdToken(true)
                .addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {
                        idToken = p0.result?.token
                        binding.progressBar.visibility = View.GONE
                        showAlertView()
                        Log.d("token", (idToken)!!)
                        // Send token to your backend via HTTPS
                        // ...
                    } else {
                        // Handle error -> task.getException();
                        binding.progressBar.visibility = View.GONE
                    }
                }
        }

    private fun callapi_registeruser(
        name: String?,
        email: String?,
        mobile_no: String,
        password: String?,
        cnf_password: String?,
        token: String?
    ) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_user(name, email, mobile_no, password, cnf_password, token)!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(
                    call: Call<JsonObject?>,
                    response: Response<JsonObject?>
                ) {
                    try {
                        if (response.code() == Constant.SUCCESS_CODE_2) {
                            val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                            //Log.d("Result", jsonObject.toString());
                            if (jsonObject.getString("status")
                                    .equals(Constant.SUCCESS_CODE_Ne, ignoreCase = true)
                            ) {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    ctx,
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                                //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));
                                storePrefrence!!.setString(
                                    Constant.NAME,
                                    jsonObject.getJSONObject("data").getString("name")
                                )
                                showAlertView_2()
                            } else {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    ctx,
                                    "Error occur please try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject: JSONObject = JSONObject(response.errorBody()!!.string())
                            if (jsonObject.getString("status").equals("422", ignoreCase = true)) {
                                val error_msg: String =
                                    jsonObject.getJSONObject("message").getJSONArray("email")
                                        .getString(0)
                                val error_msg_2: String =
                                    jsonObject.getJSONObject("message").getJSONArray("contact")
                                        .getString(0)
                                Toast.makeText(ctx, error_msg, Toast.LENGTH_SHORT).show()
                                Toast.makeText(ctx, error_msg_2, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                            } else {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    ctx,
                                    "Error occur please try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                        //stopProgress();
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show()
                    //stopProgress();
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    private fun callapi_sociallogin(token: String?, userid: String?, type: String) {
        binding.progressBar.visibility = View.VISIBLE
        Api.info.register_sociallogin(type, userid)!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val jsonObject: JSONObject = JSONObject(Gson().toJson(response.body()))
                    Log.d("Result", jsonObject.toString())
                    if (jsonObject.getString("status").equals("Success", ignoreCase = true)) {
                        Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                            .show()
                        //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));
                        if (jsonObject.getJSONObject("data").has("name")) {
                            storePrefrence!!.setString(
                                Constant.NAME,
                                jsonObject.getJSONObject("data").getString("name")
                            )
                        } else {
                            storePrefrence!!.setString(Constant.NAME, type)
                        }
                        binding.progressBar.visibility = View.GONE
                        val intent: Intent = Intent(ctx, LoginFormActivity::class.java)
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

    fun disconnectFromFacebook() {
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
        callbackManager!!.onActivityResult(
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
        if (result!!.isSuccess) {
            //gotoProfile();
            val account: GoogleSignInAccount? = result.signInAccount
            Log.d("id", (account!!.id)!!)
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_sociallogin(
                    account.idToken,
                    account.id,
                    "google"
                )
            } else {
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Sign in cancel", Toast.LENGTH_LONG).show()
        }
    }

    private fun gotoProfile() {
        //Intent intent=new Intent(RegisterActivity.this,ProfileActivity.class);
        //startActivity(intent);
        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    companion object {
        private const val RC_SIGN_IN: Int = 1
        fun isValidEmail(target: CharSequence?): Boolean {
            return (!TextUtils.isEmpty(target) && target?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true)
        }
    }
}