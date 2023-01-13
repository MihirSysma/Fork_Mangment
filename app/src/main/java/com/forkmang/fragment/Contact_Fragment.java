package com.forkmang.fragment;

import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact_Fragment extends Fragment {

    StorePrefrence storePrefrence;
    ProgressBar progressBar;
    EditText etv_username, etv_email,etv_mobile,etv_msg;
    Button btn_submit;


    public static Contact_Fragment newInstance() {
        return new Contact_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_layout, container, false);
        storePrefrence=new StorePrefrence(getContext());
        progressBar=view.findViewById(R.id.progressBar);
        etv_username=view.findViewById(R.id.etv_username);
        etv_email=view.findViewById(R.id.etv_email);
        etv_mobile=view.findViewById(R.id.etv_mobile);
        etv_msg=view.findViewById(R.id.etv_msg);
        btn_submit =view.findViewById(R.id.btn_submit);

        etv_username.setText(storePrefrence.getString(Constant.NAME));
        //etv_email.setText("test@gmail.com");
        etv_mobile.setText(storePrefrence.getString(Constant.MOBILE));
        //etv_msg.setText("Hi this is test msg");

        btn_submit.setOnClickListener(v -> {

            if(etv_username.getText().length() > 0)
            {
                if(etv_mobile.getText().toString().length() > 0)
                {
                    if(etv_mobile.getText().toString().length() == 10)
                    {
                        //Email is empty or not
                        if(Objects.requireNonNull(etv_email.getText()).length() > 0)
                        {
                            //Email is valid or not
                            if(isValidEmail(etv_email.getText().toString()))
                            {

                               callapi_contact(etv_username.getText().toString(),etv_email.getText().toString(),
                                               etv_mobile.getText().toString(), etv_msg.getText().toString());

                            }
                            else{
                                Toast.makeText(getContext(), Constant.VALIDEmail, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(getContext(), Constant.EmptyEmail, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), Constant.VALID_NO, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), Constant.ENTER_MOBILE, Toast.LENGTH_SHORT).show();
                }

            }

            else{
                Toast.makeText(getContext(), Constant.ENTER_NAME, Toast.LENGTH_SHORT).show();
            }

        });

        return view;

    }

    //Api code for Book Table start
    private void callapi_contact(String name, String email, String phone, String msg)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().contact("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                              name, email, phone, msg).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                getActivity().finish();
                                //Log.d("Result", jsonObject.toString());


                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                 Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}
