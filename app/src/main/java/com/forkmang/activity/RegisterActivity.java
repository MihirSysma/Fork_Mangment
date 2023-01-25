package com.forkmang.activity;

import static com.forkmang.helper.Constant.NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    TextInputEditText etv_username, etv_mobile, etv_password,etv_cnfpassword,etv_email;
    Button btn_register,Btn_Back;
    ImageView button_facebook,signin_button_img;
    Context ctx = RegisterActivity.this;
    StorePrefrence storePrefrence;
    ProgressBar progressBar;
    String name,mobile,password, cnfpassword,email,idToken;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    private LoginButton mButtonFacebook;
    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.progressBar);
        mButtonFacebook = findViewById(R.id.button_facebook_root);
        signInButton = findViewById(R.id.sign_in_button_root);

        button_facebook = findViewById(R.id.button_facebook);
        signin_button_img= findViewById(R.id.signin_button_img);

        Btn_Back = findViewById(R.id.Btn_Back);
        btn_register = findViewById(R.id.btn_register);
        etv_username = findViewById(R.id.etv_username);
        etv_email = findViewById(R.id.etv_email);
        etv_mobile = findViewById(R.id.etv_mobile);
        etv_password = findViewById(R.id.etv_password);
        etv_cnfpassword = findViewById(R.id.etv_cnfpassword);
        storePrefrence=new StorePrefrence(ctx);

        etv_mobile.setText("9836608967");
        etv_username.setText("967 name");
        etv_email.setText("test967@gmail.com");
        etv_password.setText("123456");
        etv_cnfpassword.setText("123456");


        //firebase login code
         mAuth=FirebaseAuth.getInstance();
         StartFirebaseLogin();
        //firebase login code end


        //facebook login code start
        printHashKey();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("id", loginResult.getAccessToken().getUserId());
                Log.d("token", loginResult.getAccessToken().getToken());

                callapi_sociallogin(loginResult.getAccessToken().getToken(),
                                    loginResult.getAccessToken().getUserId(),
                                    "facebook" );

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

        btn_register.setOnClickListener(v -> {
            //validation
            name = Objects.requireNonNull(etv_username.getText()).toString();
            mobile = Objects.requireNonNull(etv_mobile.getText()).toString();
            password = Objects.requireNonNull(etv_password.getText()).toString();
            cnfpassword = Objects.requireNonNull(etv_cnfpassword.getText()).toString();
            email = Objects.requireNonNull(etv_email.getText()).toString();


            if(etv_username.getText().length() > 0)
            {
                if(mobile.length() > 0)
                {
                    if(mobile.length() == 10)
                    {
                        //Email is empty or not
                        if(Objects.requireNonNull(etv_email.getText()).length() > 0)
                        {
                            //Email is valid or not
                            if(isValidEmail(etv_email.getText().toString()))
                            {
                                //password is valid or not
                                if(password.length() > 3)
                                {
                                    if(cnfpassword.length() > 3)
                                    {
                                        if(password.equals(cnfpassword))
                                        {
                                            //call api
                                            String phoneNumber = ("+" + "91" + etv_mobile.getText().toString());
                                            //Toast.makeText(ctx, "success", Toast.LENGTH_SHORT).show();
                                            sentRequest(phoneNumber);
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
                            }
                            else{
                                Toast.makeText(ctx, Constant.VALIDEmail, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(ctx, Constant.EmptyEmail, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(ctx, Constant.VALID_NO, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ctx, Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show();
                }

            }

            else{
                Toast.makeText(ctx, Constant.ENTER_NAME, Toast.LENGTH_SHORT).show();
            }


        });
        Btn_Back.setOnClickListener(v -> finish());

        //facebook login
        button_facebook.setOnClickListener(v -> {
            if (AccessToken.getCurrentAccessToken() == null) {
                // already logged out
                mButtonFacebook.performClick();
            }
            else{
                Toast.makeText(ctx,"Already Login wait for Logout process", Toast.LENGTH_SHORT).show();
                disconnectFromFacebook();
            }
        });


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

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.forkmang",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {


        }
    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.view_otp, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        PinView firstPinView;
        Button Btn_Submit ;
        Btn_Submit=dialogView.findViewById(R.id.btn_submit);
        firstPinView=dialogView.findViewById(R.id.firstPinView);
        firstPinView.setText("123456");


        Btn_Submit.setOnClickListener(v -> {
            //call register api
            //String phoneNumber = ("+" + "91" + etv_mobile.getText().toString());
            String phoneNumber = Objects.requireNonNull(etv_mobile.getText()).toString();
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_registeruser(name,email,phoneNumber,password,cnfpassword,idToken);
                dialog.dismiss();
            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }


        });

        dialog.show();

    }

    private void showAlertView_2()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.otp_conform, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button Btn_Done ;
        /*TextView txt_otpenter;
        txt_otpenter=dialogView.findViewById(R.id.txt_otpenter);
        txt_otpenter.setOnClickListener(v -> {
            //showAlertView();
            dialog.dismiss();
        });*/

       Btn_Done=dialogView.findViewById(R.id.btn_done);
        Btn_Done.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, LoginFormActivity.class);
                            startActivity(intent);
                            finish();
            dialog.dismiss();
        });

        dialog.show();

    }

    public void sentRequest(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                RegisterActivity.this,        // Activity (for callback binding)
                mCallback);
    }

    private void StartFirebaseLogin() {
        mAuth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("====verification complete call  " + phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                //setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), "failed");
                System.out.println("====failed" + "failed");

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                //System.out.println("====token" + forceResendingToken);
                otpVerification(verificationId,"123456");

            }
        }

        ;
    }

    public void otpVerification(String firebase_otp,String otptext)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firebase_otp, otptext);
        signInWithPhoneAuthCredential(credential, otptext);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String otptext) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            String message = "Success";
                            System.out.println("====Success" + message);
                            getToken();
                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";
                            System.out.println("====failed" + message);

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                System.out.println("====Invalid" + message);
                            }
                            //edtotp.setError(message);

                        }
                    }
                });
    }

    private void getToken() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();

                            progressBar.setVisibility(View.GONE);
                            showAlertView();
                            Log.d("token", idToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void callapi_registeruser(String name, String email, String mobile_no, String password, String cnf_password, String token)
    {
        //showProgress();
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().register_user(name,email,mobile_no,password,cnf_password, token).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_2)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());
                                if(jsonObject.getString("status").equalsIgnoreCase(Constant.SUCCESS_CODE_Ne))
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));
                                    storePrefrence.setString(NAME, jsonObject.getJSONObject("data").getString("name"));

                                    showAlertView_2();
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                                }



                            }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                               JSONObject jsonObject = new JSONObject(response.errorBody().string());
                               if(jsonObject.getString("status").equalsIgnoreCase("422"))
                               {
                                   String error_msg = jsonObject.getJSONObject("message").getJSONArray("email").getString(0) ;
                                   String error_msg_2 = jsonObject.getJSONObject("message").getJSONArray("contact").getString(0);
                                   Toast.makeText(ctx,error_msg,Toast.LENGTH_SHORT).show();
                                   Toast.makeText(ctx,error_msg_2,Toast.LENGTH_SHORT).show();
                                   progressBar.setVisibility(View.GONE);
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
                                //storePrefrence.setString(TOKEN_REG, jsonObject.getJSONObject("data").getString("token"));

                                if(jsonObject.getJSONObject("data").has("name"))
                                {
                                    storePrefrence.setString(NAME, jsonObject.getJSONObject("data").getString("name"));
                                }
                                else{
                                    storePrefrence.setString(NAME, type);
                                }

                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(ctx, LoginFormActivity.class);
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


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
                    }
                })
                .executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        // add this line
        callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data);

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            //gotoProfile();
            GoogleSignInAccount account=result.getSignInAccount();
            Log.d("id", account.getId());

            if (Utils.isNetworkAvailable(ctx)) {
                callapi_sociallogin(account.getIdToken(),
                        account.getId(),
                        "google" );
            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

            }






        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        //Intent intent=new Intent(RegisterActivity.this,ProfileActivity.class);
        //startActivity(intent);
        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}