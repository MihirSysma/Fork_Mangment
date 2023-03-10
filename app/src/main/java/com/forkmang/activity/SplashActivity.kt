package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.facebook.stetho.Stetho
import com.forkmang.R
import com.forkmang.helper.Constant
import com.forkmang.helper.StorePrefrence
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*

class SplashActivity : AppCompatActivity() {
    private val storePrefrence by lazy { StorePrefrence(this) }
    var ctx: Context = this@SplashActivity
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        Stetho.initializeWithDefaults(this)
        /*Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/

        /* addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/


        //loadLocale();
        gotoNextScreeen()
        //getToken();
    }

    /* private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SplashActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                if (!mToken.equals(session.getData(Session.KEY_FCM_ID))) {
                    //UpdateToken(mToken, MainActivity.this);
                }
                Log.e("Token",mToken);
            }
        });

    }*/
    private fun loadLocale() {
        val langPref: String = "Language"
        val prefs: SharedPreferences = getSharedPreferences(
            "CommonPrefs",
            MODE_PRIVATE
        )
        val language: String? = prefs.getString(langPref, "")
        changeLang(language)
    }

    private fun changeLang(lang: String?) {
        if (lang.equals("", ignoreCase = true)) return
        val myLocale: Locale? = lang?.let { Locale(it) }
        saveLocale(lang)
        if (myLocale != null) {
            Locale.setDefault(myLocale)
        }
        val config: Configuration = Configuration()
        config.locale = myLocale
        baseContext.resources
            .updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun saveLocale(lang: String?) {
        val langPref: String = "Language"
        val prefs: SharedPreferences = getSharedPreferences(
            "CommonPrefs",
            MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(langPref, lang)
        editor.apply()
    }

    private fun gotoNextScreeen() {
        Handler().postDelayed({

            if(storePrefrence.getString(Constant.NAME)?.length == 0)
            {
                val intentRegister = Intent(this, RegisterActivity::class.java)
                startActivity(intentRegister);
                finish();
            }
/*            else if (storePrefrence?.getBoolean("keeplogin") == true){
                val dash_board_intent = Intent(this, DashBoardActivity_2::c ->  });
                startActivity(dash_board_intent);
                finish();
            }*/
            else if(storePrefrence.getBoolean("keeplogin").not())
            {
                val intentLoginForm = Intent(this, LoginFormActivity::class.java)
                startActivity(intentLoginForm)
                finish();
            }
            else {
                val intentDashBoard = Intent(this@SplashActivity, DashBoardActivity::class.java)
                startActivity(intentDashBoard)
                finish()
            }

        }, 2000)
    }
}