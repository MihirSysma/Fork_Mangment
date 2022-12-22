package com.forkmang.activity;

import static com.forkmang.helper.Constant.NAME;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.network_call.Api;
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

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    TextInputEditText etv_username, etv_mobile, etv_password,etv_cnfpassword;
    Button btn_register,Btn_Back;
    Context ctx = RegisterActivity.this;
    StorePrefrence storePrefrence;
    private ProgressDialog dialog;
    String name,mobile,password, cnfpassword,idToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Btn_Back = findViewById(R.id.Btn_Back);
        btn_register = findViewById(R.id.btn_register);
        etv_username = findViewById(R.id.etv_username);
        etv_mobile = findViewById(R.id.etv_mobile);
        etv_password = findViewById(R.id.etv_password);
        etv_cnfpassword = findViewById(R.id.etv_cnfpassword);
        storePrefrence=new StorePrefrence(ctx);
        mAuth=FirebaseAuth.getInstance();


        /*etv_username.setText("Testusername1");
        etv_mobile.setText("9829020400");
        etv_password.setText("123456");
        etv_cnfpassword.setText("123456");*/

        StartFirebaseLogin();

        btn_register.setOnClickListener(v -> {
            //validation
            boolean isValid = true;
            name = etv_username.getText().toString();
            mobile = etv_mobile.getText().toString();
            password = etv_password.getText().toString();
            cnfpassword = etv_cnfpassword.getText().toString();

            if(etv_username.getText().length() > 0)
            {
                if(mobile.length() > 0)
                {
                    if(mobile.length() == 10)
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
                        Toast.makeText(ctx, Constant.VALID_NO, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ctx, Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show();
                }

            }

            else{
                Toast.makeText(ctx, Constant.ENTER_NAME, Toast.LENGTH_SHORT).show();
                isValid = false;
            }


        });

        Btn_Back.setOnClickListener(v -> finish());
    }


    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.view_otp, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button Btn_Submit ;
        Btn_Submit=dialogView.findViewById(R.id.btn_submit);
        Btn_Submit.setOnClickListener(v -> {
            //call register api
            //String phoneNumber = ("+" + "91" + mobile);
            String phoneNumber = "9829020400";
            callapi_registeruser(name,"test400@gmail.com",phoneNumber,password,cnfpassword,idToken);


            dialog.dismiss();
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

                            showAlertView();

                            Log.d("token", idToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }

    private void callapi_registeruser(String name, String email, String mobile_no, String password, String cnf_password, String token)
    {
        showProgress();
        Api.getInfo().register_user(name,email,mobile_no,password,cnf_password, token).
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
                                storePrefrence.setString(NAME, jsonObject.getJSONObject("data").getString("name"));
                                stopProgress();
                                showAlertView_2();
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