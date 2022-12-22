package com.forkmang.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.StorePrefrence;

import java.util.Locale;



public class SplashActivity extends AppCompatActivity {
    StorePrefrence storePrefrence;
    Context ctx = SplashActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        storePrefrence=new StorePrefrence(ctx);
        //loadLocale();
        GotoNextScreeen();
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



    private void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }


    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                SplashActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }



    private void GotoNextScreeen()
    {
        new Handler().postDelayed(() -> {
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

            final Intent mainIntent = new Intent(SplashActivity.this, LoginFormActivity.class);
            startActivity(mainIntent);
            finish();

            /*final Intent mainIntent = new Intent(SplashActivity.this, Booking_TabView_Activity.class);
            startActivity(mainIntent);
            finish();*/

        }, 2000);
    }

}