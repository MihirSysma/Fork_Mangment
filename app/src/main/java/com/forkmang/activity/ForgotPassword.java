package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE_Ne;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.network_call.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    TextInputEditText etv_newpas, etvcnf_pass;
    Context ctx = ForgotPassword.this;
    StorePrefrence storePrefrence;
    private ProgressDialog dialog;
    Button btn_reset;
    String idToken;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        storePrefrence=new StorePrefrence(ctx);
        progressBar = findViewById(R.id.progressBar);
        btn_reset = findViewById(R.id.btn_reset);
        etv_newpas = findViewById(R.id.etv_newpas);
        etvcnf_pass = findViewById(R.id.etvcnf_pass);

        etv_newpas.setText("1234567");
        etvcnf_pass.setText("1234567");



        btn_reset.setOnClickListener(v -> {
           /* final Intent mainIntent = new Intent(ForgotPassword.this, FaceLoginPermission.class);
            startActivity(mainIntent);
            finish();*/

            if(etv_newpas.getText().length() > 0)
            {
                if(etvcnf_pass.getText().length() > 0)
                {
                    if(etv_newpas.getText().toString().equals(etvcnf_pass.getText().toString()))
                    {
                        //call api
                        String contact = storePrefrence.getString(Constant.MOBILE);
                        getToken(contact);
                    }
                    else{
                        Toast.makeText(ctx, Constant.PASSWORD_MATCH, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ctx, Constant.CNFPASSWORD_MATCH, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(ctx, Constant.PASSWORD, Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void callApi_forgetpassword_valid(String contact) {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().forgot_pass(contact, idToken).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_2)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE_Ne))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    storePrefrence.setString(Constant.MOBILE, jsonObject.getJSONObject("data").getString("contact"));
                                    //storePrefrence.setString(Constant.TOKEN_FORGOTPASS, jsonObject.getJSONObject("data").getString("token"));
                                    storePrefrence.setBoolean("keeplogin", false);


                                    if (Utils.isNetworkAvailable(ctx)) {
                                        callApi_resetpassword(jsonObject.getJSONObject("data").getString("contact"),
                                                Objects.requireNonNull(etv_newpas.getText()).toString(),
                                                Objects.requireNonNull(etvcnf_pass.getText()).toString(),
                                                jsonObject.getJSONObject("data").getString("token") );

                                    }
                                    else{
                                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                                    }


                                    //stopProgress();
                                }
                                else{
                                    //stopProgress();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if(jsonObject.getString("status").equalsIgnoreCase(String.valueOf(Constant.ERROR_CODE)))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    String error_msg = jsonObject.getString("message") ;
                                    Toast.makeText(ctx,error_msg,Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }


                            }
                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            //stopProgress();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        //stopProgress();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void callApi_resetpassword(String contact, String password, String cnf_password, String token) {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().reset_pass(contact, password, cnf_password, token).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_2)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE_Ne))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    final Intent mainIntent = new Intent(ForgotPassword.this, LoginFormActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                                else{
                                    //stopProgress();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                if(jsonObject.getString("status").equalsIgnoreCase(String.valueOf(Constant.ERROR_CODE)))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    String error_msg = jsonObject.getString("message") ;
                                    Toast.makeText(ctx,error_msg,Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }


                            }
                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            //stopProgress();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        //stopProgress();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }


    private void getToken(String contact) {
        //progressBar.setVisibility(View.VISIBLE);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            Log.d("token", idToken);
                            //progressBar.setVisibility(View.GONE);



                            if (Utils.isNetworkAvailable(ctx)) {
                                callApi_forgetpassword_valid(contact);
                            }
                            else{
                                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                            }

                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}