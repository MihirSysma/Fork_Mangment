package com.forkmang.fragment;

import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.TableOrdersListing_Adapter;
import com.forkmang.data.TableOrderListing;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Table_OrdersListing_Fragment extends Fragment {
    RecyclerView recyclerView;
    StorePrefrence storePrefrence;
    ProgressBar progressBar;
    ArrayList<TableOrderListing> tableOrderListingArrayList;
    public static Table_OrdersListing_Fragment instance;


    public static Table_OrdersListing_Fragment newInstance() {
        return new Table_OrdersListing_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booktable_layout_orders, container, false);
        instance= this;
        storePrefrence=new StorePrefrence(getContext());
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.booktable_recycleview_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isNetworkAvailable(getContext())) {
            callapi_gettableorderslist();
        }
        else{
            Toast.makeText(getContext(), Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

        }

    }



    //Api code for Book Table start
    private void callapi_gettableorderslist()
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getbooktable_listing("Bearer "+storePrefrence.getString(TOKEN_LOGIN)).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());

                                if(jsonObject.getString("status").equalsIgnoreCase(Constant.SUCCESS_CODE))
                                {
                                    if(jsonObject.getJSONArray("data").length() > 0)
                                    {
                                        TableOrderListing tableOrderListing = new TableOrderListing();
                                        tableOrderListingArrayList = new ArrayList<TableOrderListing>();
                                        for(int i = 0; i< jsonObject.getJSONArray("data").length(); i++)
                                        {
                                            JSONObject mjsonobj = jsonObject.getJSONArray("data").getJSONObject(i);

                                            tableOrderListing.setOrder_id(mjsonobj.getString("id"));
                                            tableOrderListing.setUser_id(mjsonobj.getString("user_id"));
                                            tableOrderListing.setOrder_total(mjsonobj.getString("total"));
                                            tableOrderListing.setPayment_status(mjsonobj.getString("payment_status"));
                                            tableOrderListing.setOrder_status(mjsonobj.getString("order_status"));
                                            tableOrderListing.setResturant_id(mjsonobj.getString("restaurant_id"));

                                            tableOrderListing.setStr_restroname(mjsonobj.getJSONObject("restaurant").getString("rest_name"));
                                            tableOrderListing.setResturant_branch(mjsonobj.getJSONObject("restaurant").getString("rest_branch"));
                                            tableOrderListing.setResturant_contact(mjsonobj.getJSONObject("restaurant").getString("contact"));

                                            if(mjsonobj.isNull(mjsonobj.getString("timing")))
                                            {
                                                tableOrderListing.setResturant_timing("00");
                                            }
                                            else{
                                                tableOrderListing.setResturant_timing(mjsonobj.getString("timing"));
                                            }
                                            tableOrderListingArrayList.add(tableOrderListing);
                                        }

                                        TableOrdersListing_Adapter bookTableAdapter_orders = new TableOrdersListing_Adapter(getActivity(),tableOrderListingArrayList);
                                        recyclerView.setAdapter(bookTableAdapter_orders);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                    else{
                                        //no data in array list
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), Constant.NODATA, Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                                }
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



    //Fragment Instance
    public static Table_OrdersListing_Fragment GetInstance()
    {
        return instance;
    }
}
