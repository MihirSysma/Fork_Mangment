package com.forkmang.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.network_call.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFormActivity extends AppCompatActivity {

    TextInputEditText etv_mobile, etv_password;
    Context ctx = LoginFormActivity.this;
    private ProgressDialog dialog;
    StorePrefrence storePrefrence;
    CheckBox chek_keeplogin;
    String token="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        storePrefrence=new StorePrefrence(ctx);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        Button BtnReg = findViewById(R.id.BtnReg);
        Button BtnLogin = findViewById(R.id.BtnLogin);
        etv_mobile = findViewById(R.id.etv_mobile);
        etv_password = findViewById(R.id.etv_password);
        chek_keeplogin = findViewById(R.id.chek_keeplogin);


        etv_mobile.setText("9829020400");
        etv_password.setText("123456");

        chek_keeplogin.setOnClickListener(v -> {
            if(etv_mobile.length()==10 && etv_password.length() >3)
            {
                if(chek_keeplogin.isChecked())
                {
                    Constant.KEEP_LOGIN=true;
                }
                else if (!chek_keeplogin.isChecked()){
                    Constant.KEEP_LOGIN=false;
                }
            }
            else{
                Toast.makeText(ctx,Constant.MOBILE_PASSWORD, Toast.LENGTH_SHORT).show();
            }
       });

        BtnLogin.setOnClickListener(v -> {
            if(etv_mobile.length() > 0)
            {
                if(etv_mobile.length() == 10)
                {
                    //password is valid or not
                    if(etv_password.length() > 3)
                    {
                         callapi_loginuser(etv_mobile.getText().toString(), etv_password.getText().toString());
                        //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ctx, Constant.PASSWORD, Toast.LENGTH_SHORT).show();
                      }
                }
                else{
                    Toast.makeText(ctx, Constant.VALID_NO, Toast.LENGTH_SHORT).show();
                  }
            }
            else{
                Toast.makeText(ctx, Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show();
            }
        });

        txtForgotPassword.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginFormActivity.this, ForgotPassword.class);
            startActivity(mainIntent);
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            //finish();
        });

        BtnReg.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(LoginFormActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }


    private void callapi_loginuser(String contact, String password)
    {
        showProgress();
        Api.getInfo().login_user(contact,password).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Result", jsonObject.toString());
                            if(jsonObject.getString("status").equalsIgnoreCase("Success"))
                            {
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                storePrefrence.setBoolean("keeplogin", Constant.KEEP_LOGIN);
                                storePrefrence.setString(Constant.MOBILE, etv_mobile.getText().toString());

                                //storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                /*if(chek_keeplogin.isChecked())
                                {
                                    storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                }*/

                                 stopProgress();
                                 final Intent mainIntent = new Intent(ctx, DashBoardActivity_2.class);
                                 startActivity(mainIntent);
                                 finish();
                             }
                            else{
                                Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                            }
                            stopProgress();
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            stopProgress();
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        stopProgress();
                    }
                });
       }




    public void showProgress() {
        dialog = new ProgressDialog(ctx);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void stopProgress(){
        dialog.dismiss();
    }


}