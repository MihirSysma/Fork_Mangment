package com.forkmang.fragment;

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
import com.forkmang.adapter.Pickup_Fragment_BookTableAdapter;
import com.forkmang.data.RestoData;
import com.forkmang.helper.ApiConfig;
import com.forkmang.helper.Constant;
import com.forkmang.helper.GPSTracker;
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

public class Pickup_Fragment extends Fragment {

    private static Pickup_Fragment instance;
    RecyclerView recyclerView;
    ArrayList<RestoData> restoDataArrayList;
    GPSTracker gps;
    Double saveLatitude,saveLongitude;
    ProgressBar progressBar;

    public static Pickup_Fragment newInstance() {
        return new Pickup_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_layout, container, false);
        instance= this;

        recyclerView = view.findViewById(R.id.pick_recycleview);
        progressBar = view.findViewById(R.id.progressbar);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setHasFixedSize(true);
        //GET GPS Current start
        ApiConfig.getLocation(getActivity());
        gps = new GPSTracker(getActivity());
        saveLatitude = gps.latitude;
        saveLongitude = gps.longitude;

        if (gps.getIsGPSTrackingEnabled())
        {
            saveLatitude = gps.latitude;
            saveLongitude = gps.longitude;
        }
        //GET GPS Current end
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        String service_id = "3";
        saveLatitude = 23.933689;
        saveLongitude = 72.367458;
        callapi_getbooktable(service_id, String.valueOf(saveLatitude), String.valueOf(saveLongitude));
        //((Booking_TabView_Activity)getActivity()).hide_search();
    }

    //Api code for Book Table start
    private void callapi_getbooktable(String service_id, String latitude, String logitutde)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getlist_res_walkin(service_id,latitude, logitutde).
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
                                    if(jsonObject.getJSONObject("data").getJSONArray("data").length() > 0)
                                    {
                                        restoDataArrayList = new ArrayList<>();
                                        for(int i =0; i< jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                        {
                                            RestoData bookTable = new RestoData();
                                            JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i);

                                            //JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0);
                                            bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        /*if(i > 0)
                                        {
                                            bookTable.setRest_name("REST"+" "+i);
                                        }
                                        else{
                                            bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        }*/
                                            if(mjson_obj.has("endtime"))
                                            {
                                                bookTable.setEndtime(mjson_obj.getString("endtime"));
                                            }
                                            else{
                                                bookTable.setEndtime("00");
                                            }
                                            bookTable.setId(mjson_obj.getString("id"));
                                            double double_val = Math.floor(mjson_obj.getDouble("distance") * 100) / 100;
                                            bookTable.setDistance(Double.toString(double_val));

                                            restoDataArrayList.add(bookTable);
                                        }
                                        progressBar.setVisibility(View.GONE);

                                        Pickup_Fragment_BookTableAdapter pickup_fragment_bookTableAdapter = new Pickup_Fragment_BookTableAdapter(getActivity(),restoDataArrayList, "listing",getContext());
                                        recyclerView.setAdapter(pickup_fragment_bookTableAdapter);

                                    }
                                    else{
                                        //no data in array list
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), Constant.NODATA, Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            //Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void callapi_searchbooktable(String search_strq, String latitude, String logitutde)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getlist_searchres(search_strq, latitude, logitutde).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            //Log.d("Result", jsonObject.toString());

                            if(jsonObject.getString("status").equalsIgnoreCase(Constant.SUCCESS_CODE))
                            {

                                if(jsonObject.getJSONObject("data").getJSONArray("data").length() > 0)
                                {
                                    restoDataArrayList = new ArrayList<>();
                                    for(int i =0; i<jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                    {
                                        RestoData bookTable = new RestoData();
                                        JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i);
                                        bookTable.setRest_name(mjson_obj.getString("rest_name"));

                                        if(mjson_obj.has("endtime"))
                                        {
                                            bookTable.setEndtime(mjson_obj.getString("endtime"));
                                        }
                                        else{
                                            bookTable.setEndtime("00");
                                        }
                                        bookTable.setId(mjson_obj.getString("id"));
                                        double double_val = Math.floor(mjson_obj.getDouble("distance") * 100) / 100;
                                        bookTable.setDistance(Double.toString(double_val));

                                        restoDataArrayList.add(bookTable);
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    Pickup_Fragment_BookTableAdapter pickup_fragment_bookTableAdapter = new Pickup_Fragment_BookTableAdapter(getActivity(), restoDataArrayList, "listing",getContext());
                                    recyclerView.setAdapter(pickup_fragment_bookTableAdapter);



                                }
                                else{
                                    //no data in array list
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), Constant.NODATA, Toast.LENGTH_LONG).show();
                                }
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

    public  void filter_booktable(String search_str)
    {
        if (Utils.isNetworkAvailable(getContext())) {
            callapi_searchbooktable(search_str,saveLatitude.toString(),saveLongitude.toString());
        }
        else{
            Toast.makeText(getContext(), Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
        }
    }

    public void call_reloadbooktable()
    {
        if (Utils.isNetworkAvailable(getContext())) {
            saveLatitude=21.209589;
            saveLongitude=72.860824;
            String service_id = "3";
            callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString());
        }
        else{
            Toast.makeText(getContext(), Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
        }
    }

    //Api code for Book Table end
    public static Pickup_Fragment GetInstance()
    {
        return instance;
    }
}
