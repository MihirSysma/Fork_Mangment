package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    StorePrefrence storePrefrence;
    Context ctx = LoginActivity.this;
    ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button BtnLogin = findViewById(R.id.BtnLogin);
        Button BtnReg = findViewById(R.id.BtnReg);
        TextView TextGuest = findViewById(R.id.TextGuest);
        progress_bar= findViewById(R.id.progress_bar);
        storePrefrence=new StorePrefrence(ctx);

        TextGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(Utils.isNetworkAvailable(ctx))
              {
                  callapi_loginasguest();
              }
              else{
                  Toast.makeText(ctx,Constant.NETWORKEROORMSG,Toast.LENGTH_SHORT).show();
              }
            }
        });


        BtnReg.setOnClickListener(v -> {
            if(storePrefrence.getString(Constant.NAME).length() == 0)
            {
                final Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mainIntent);
                //finish();
            }
            else{
                Toast.makeText(ctx,"User already registered please click login", Toast.LENGTH_LONG).show();
            }
            //finish();
        });


        BtnLogin.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginActivity.this, LoginFormActivity.class);
            startActivity(mainIntent);
            //finish();
        });
    }


    private void callapi_loginasguest()
    {
        //showProgress();
        progress_bar.setVisibility(View.VISIBLE);
        String identifier="identifier123";

        Api.getInfo().login_guest(identifier).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{

                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE))
                                {
                                    progress_bar.setVisibility(View.GONE);
                                    storePrefrence.setString(Constant.IDENTFIER, jsonObject.getJSONObject("data").getString("identifier"));
                                    storePrefrence.setString(Constant.TOKEN_LOGIN,"");
                                    storePrefrence.setString("id", jsonObject.getJSONObject("data").getString("id"));

                                    final Intent mainIntent = new Intent(ctx, DashBoard_Activity.class);
                                    startActivity(mainIntent);
                                    finish();

                                }
                                else{
                                    progress_bar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if(jsonObject.getString("status").equalsIgnoreCase(String.valueOf(Constant.ERROR_CODE)))
                                {
                                    progress_bar.setVisibility(View.GONE);
                                    String error_msg = jsonObject.getString("message") ;
                                    Toast.makeText(ctx,error_msg,Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    progress_bar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }


                            }
                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        //stopProgress();
                        progress_bar.setVisibility(View.GONE);
                    }
                });
    }
}