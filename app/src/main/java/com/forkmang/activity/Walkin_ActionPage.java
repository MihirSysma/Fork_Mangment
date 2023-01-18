package com.forkmang.activity;

import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.forkmang.R;
import com.forkmang.data.RestoData;
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

public class Walkin_ActionPage extends AppCompatActivity {

    TextView txt_queueno,txt_restoadd,txt_restophone,txt_restroname;
    ProgressBar progressBar;
    StorePrefrence storePrefrence;
    Context ctx = Walkin_ActionPage.this;
    String identifier,quee_no, noofperson,occasion,area;
    RestoData restromodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkin_action);
        storePrefrence=new StorePrefrence(ctx);
        txt_queueno = findViewById(R.id.txt_queueno);
        ImageView img_pass = findViewById(R.id.img_pass);
        ImageView img_cancel = findViewById(R.id.img_cancel);
        ImageView img_exit = findViewById(R.id.img_exit);
        progressBar = findViewById(R.id.progressBar);
        txt_restoadd= findViewById(R.id.txt_restoadd);
        txt_restophone= findViewById(R.id.txt_restophone);
        txt_restroname= findViewById(R.id.txt_restroname);

        Intent intent = getIntent();
        quee_no = intent.getStringExtra("quee_no");
        noofperson = intent.getStringExtra("person");
        occasion = intent.getStringExtra("occasion");
        area = intent.getStringExtra("area");

        restromodel = (RestoData) getIntent().getSerializableExtra("restromodel");
        txt_queueno.setText("No in current queue: "+quee_no);

        txt_restroname.setText(restromodel.getRest_name());

        //txt_restophone.setText(storePrefrence.getString(Constant.MOBILE));

        identifier="";
        img_pass.setOnClickListener(v -> {
            if(Utils.isNetworkAvailable(ctx))
            {
                callapi_action("accept", identifier);
            }
            else{
                Toast.makeText(ctx,Constant.NETWORKEROORMSG,Toast.LENGTH_SHORT).show();
            }

        });

        img_cancel.setOnClickListener(v -> {
            if(Utils.isNetworkAvailable(ctx))
            {
                callapi_action("cancel", identifier);
            }
            else{
                Toast.makeText(ctx,Constant.NETWORKEROORMSG,Toast.LENGTH_SHORT).show();
            }


        });

        img_exit.setOnClickListener(v -> {
            if(Utils.isNetworkAvailable(ctx))
            {
                callapi_action("exit", identifier);
            }
            else{
                Toast.makeText(ctx,Constant.NETWORKEROORMSG,Toast.LENGTH_SHORT).show();
            }

        });

        txt_queueno.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(Walkin_ActionPage.this, Activity_PaymentSummary_WalkinFragment.class);
            startActivity(mainIntent);
        });

    }

    private void callapi_action(String action, String identifier)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().queue_action("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                        action,restromodel.getId(), noofperson,occasion,area,identifier).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else if(response.code() == Constant.ERROR_CODE_n){
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();

                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                });
    }

    
}