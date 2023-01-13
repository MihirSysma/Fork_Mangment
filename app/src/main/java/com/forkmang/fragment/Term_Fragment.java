package com.forkmang.fragment;

import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Term_Fragment extends Fragment {

    StorePrefrence storePrefrence;
    ProgressBar progressBar;
    TextView txt_term;
    public static Term_Fragment newInstance() {
        return new Term_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_layout, container, false);
        storePrefrence=new StorePrefrence(getContext());
        progressBar=view.findViewById(R.id.progressBar);
        txt_term = view.findViewById(R.id.txt_term);

        callapi_terms();

        return view;

    }


    //Api code for Book Table start
    private void callapi_terms()
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getterms("Bearer "+storePrefrence.getString(TOKEN_LOGIN)).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                JSONArray data_array = jsonObject.getJSONObject("data").getJSONArray("data");
                                String term = data_array.getJSONObject(0).getString("content");
                                txt_term.setText(term);
                                progressBar.setVisibility(View.GONE);

                                //Log.d("Result", jsonObject.toString());


                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                txt_term.setText("Error Occur During  Fetching Terms & Condition");
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            txt_term.setText("Error Occur During  Fetching Terms & Condition");
                            //Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        txt_term.setText("Error Occur During  Fetching Terms & Condition");
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }





}
