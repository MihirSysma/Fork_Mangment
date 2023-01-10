package com.forkmang.activity;

import static com.forkmang.helper.Constant.NAME;
import static com.forkmang.helper.Constant.SUCCESS_CODE;
import static com.forkmang.helper.Constant.TOKEN_REG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.network_call.Api;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFormActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private CallbackManager callbackManager;
    TextInputEditText etv_mobile, etv_password;
    Context ctx = LoginFormActivity.this;
    private ProgressDialog dialog;
    StorePrefrence storePrefrence;
    CheckBox chek_keeplogin;
    String token="";
    ProgressBar progressBar;
    ImageView button_facebook,signin_button_img;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    private LoginButton mButtonFacebook;
    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        storePrefrence=new StorePrefrence(ctx);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        mButtonFacebook = findViewById(R.id.button_facebook_root);
        signInButton = findViewById(R.id.sign_in_button_root);

        button_facebook = findViewById(R.id.button_facebook);
        signin_button_img= findViewById(R.id.signin_button_img);

        Button BtnReg = findViewById(R.id.BtnReg);
        Button BtnLogin = findViewById(R.id.BtnLogin);
        etv_mobile = findViewById(R.id.etv_mobile);
        etv_password = findViewById(R.id.etv_password);
        chek_keeplogin = findViewById(R.id.chek_keeplogin);

        //facebook login code start
        //printHashKey();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("id", loginResult.getAccessToken().getUserId());
                Log.d("token", loginResult.getAccessToken().getToken());

                if (Utils.isNetworkAvailable(ctx)) {
                    callapi_sociallogin(loginResult.getAccessToken().getToken(),
                            loginResult.getAccessToken().getUserId(),
                            "facebook" );
                }
                else{
                    Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

                }




            }

            @Override
            public void onCancel() {
                Log.d("res", "cancle");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("res", "error");
            }
        });
        //facebook login code end

        //google login code start
        google_intialization();
        // google login code end

        etv_mobile.setText("9829020300");
        etv_password.setText("1234567");

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
                        if (Utils.isNetworkAvailable(ctx)) {
                            callapi_loginuser(etv_mobile.getText().toString(), etv_password.getText().toString());
                        }
                        else{
                            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                        }


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

        //facebook login
        button_facebook.setOnClickListener(v -> {
            if (AccessToken.getCurrentAccessToken() == null) {
                // already logged out
                mButtonFacebook.performClick();
            }
            else{
                // already login logout
                Toast.makeText(ctx,"Already Login wait for logout process", Toast.LENGTH_SHORT).show();
                disconnectFromFacebook();
            }
        });


        //google login
        signin_button_img.setOnClickListener(v -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,RC_SIGN_IN);
        });
    }

    private void google_intialization() {
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }


    private void callapi_loginuser(String contact, String password)
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);

        Api.getInfo().login_user(contact,password).
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
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    storePrefrence.setBoolean("keeplogin", Constant.KEEP_LOGIN);
                                    storePrefrence.setString(Constant.MOBILE, jsonObject.getJSONObject("data").getString("contact"));
                                    storePrefrence.setString(Constant.NAME, jsonObject.getJSONObject("data").getString("name"));
                                    storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                    /*if(chek_keeplogin.isChecked())
                                    {
                                        storePrefrence.setString(Constant.TOKEN_LOGIN, jsonObject.getJSONObject("data").getString("token"));
                                    }*/

                                    //stopProgress();
                                    progressBar.setVisibility(View.GONE);

                                    final Intent mainIntent = new Intent(ctx, DashBoard_Activity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                                else{
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


    private void callapi_sociallogin(String token, String userid, String type)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().register_sociallogin(type,userid).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Result", jsonObject.toString());

                            if(jsonObject.getString("status").equalsIgnoreCase("Success"))
                            {
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));

                                if(jsonObject.getJSONObject("data").has("name"))
                                {
                                    storePrefrence.setString(NAME, jsonObject.getJSONObject("data").getString("name"));
                                }
                                else{
                                    storePrefrence.setString(NAME, type);
                                }

                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(ctx, DashBoard_Activity.class);
                                startActivity(intent);
                                finish();

                            }
                            else{
                                Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                        mButtonFacebook.performClick();

                    }
                })
                .executeAsync();
    }
}