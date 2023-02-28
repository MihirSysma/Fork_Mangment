package com.forkmang.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R
import com.forkmang.helper.StorePrefrence
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*

class SplashActivity : AppCompatActivity() {
    var storePrefrence: StorePrefrence? = null
    var ctx: Context = this@SplashActivity
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        storePrefrence = StorePrefrence(ctx)

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
        GotoNextScreeen()
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

    fun changeLang(lang: String?) {
        if (lang.equals("", ignoreCase = true)) return
        val myLocale: Locale = Locale(lang)
        saveLocale(lang)
        Locale.setDefault(myLocale)
        val config: Configuration = Configuration()
        config.locale = myLocale
        baseContext.resources
            .updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    fun saveLocale(lang: String?) {
        val langPref: String = "Language"
        val prefs: SharedPreferences = getSharedPreferences(
            "CommonPrefs",
            MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString(langPref, lang)
        editor.commit()
    }

    private fun GotoNextScreeen() {
        Handler().postDelayed({


            /*if(storePrefrence.getString(Constant.NAME).length() == 0)
             {
                 final Intent register_intent = new Intent(SplashActivity.this, RegisterActivity.class);
                 startActivity(register_intent);
                 finish();
             }
             else if (storePrefrence.getBoolean("keeplogin")){
                 final Intent dash_board_intent = new Intent(SplashActivity.this, DashBoardActivity_2.class);
                 startActivity(dash_board_intent);
                 finish();
             }
             else if(!storePrefrence.getBoolean("keeplogin"))
             {
                 final Intent dash_board_intent = new Intent(SplashActivity.this, LoginFormActivity.class);
                 startActivity(dash_board_intent);
                 finish();
             }*/
            val mainIntent: Intent = Intent(this@SplashActivity, DashBoard_Activity::class.java)
            startActivity(mainIntent)
            finish()
        }, 2000)
    }
}