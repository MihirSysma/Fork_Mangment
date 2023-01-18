package com.forkmang.fragment;

import static com.forkmang.helper.Constant.SUCCESS_CODE;
import static com.forkmang.helper.Constant.TOKEN_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.Walkin_ActionPage;
import com.forkmang.adapter.SpinnnerAdapter;
import com.forkmang.adapter.SpinnnerAdapter_Type;
import com.forkmang.adapter.SpinnnerAdapter_ocassion;
import com.forkmang.adapter.Walkin_listing_Adapter;
import com.forkmang.data.AreaDropdown;
import com.forkmang.data.RestoData;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Walkin_detail_Fragment extends FragmentActivity {
    //Walkin_detail_Fragment instance;
    RecyclerView recyclerView;
    LinearLayout get_inquee;
    RelativeLayout relative_bottom;
    Context ctx = Walkin_detail_Fragment.this;
    public static Walkin_detail_Fragment newInstance() {
        return new Walkin_detail_Fragment();
    }
    String resturant_id,str_area="Indoor";
    RestoData restoData;
    ProgressBar progressBar;
    Double saveLatitude,saveLongitude;
    ArrayList<RestoData> restoDataArrayList;
    Walkin_listing_Adapter walkin_listing_adapter;
    StorePrefrence storePrefrence;
    String quee_no,action, noof_person,occasion,area,identifier="",str_area_id;
    TextView txt_queeno,txtqno_bottom,txtnextview;
    Spinner spinner_type,spinner_person,spinner_ocassion;
    ArrayList<AreaDropdown> areaDropdownArrayList;
    Boolean is_areatype=false,is_pesonselect=false,is_occasionselect=false;
    String [] person_arr =
            {"Select Person","1","2 ","3","4","5","6","7","8","9","10"};
    String [] occasion_arr =
            {"Select Occasion","Birthday","Anniversary","Marriage"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_walkindetail_layout);
        //instance= this;
        storePrefrence= new StorePrefrence(ctx);
        get_inquee = findViewById(R.id.get_inquee);
        txt_queeno = findViewById(R.id.txt_queeno);
        txtqno_bottom = findViewById(R.id.txtqno_bottom);
        recyclerView = findViewById(R.id.walkin_recycleview);
        progressBar = findViewById(R.id.progressBar);
        relative_bottom = findViewById(R.id.relative_bottom);
        txtnextview = findViewById(R.id.txtnextview);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setHasFixedSize(true);

        spinner_type= findViewById(R.id.spinner_type);
        spinner_person= findViewById(R.id.spinner);
        spinner_ocassion= findViewById(R.id.spinner_ocassion);

        Intent intent = getIntent();
        resturant_id = intent.getStringExtra("resturant_id");
        restoData = (RestoData) getIntent().getSerializableExtra("restromodel");


        //spinner_person array adapter start
        SpinnnerAdapter personAdapter=new SpinnnerAdapter(getApplicationContext(), person_arr);
        spinner_person.setAdapter(personAdapter);
        spinner_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                is_pesonselect=false;
                if(position > 0)
                {
                    Toast.makeText(ctx,person_arr[position],Toast.LENGTH_SHORT).show();
                    noof_person = person_arr[position];
                    String[] arrOfStr = noof_person.split(" ", 2);
                    noof_person=arrOfStr[0];
                    is_pesonselect=true;
                }
                else{
                    is_pesonselect=false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_person array adapter end


        //spinner_occasion array adapter start
        SpinnnerAdapter_ocassion ocassionAdapter=new SpinnnerAdapter_ocassion(getApplicationContext(), occasion_arr);
        spinner_ocassion.setAdapter(ocassionAdapter);
        spinner_ocassion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                is_occasionselect=false;
                if(position > 0)
                {
                    Toast.makeText(ctx,occasion_arr[position],Toast.LENGTH_SHORT).show();
                    occasion = occasion_arr[position];
                    is_occasionselect=true;
                }
                else{
                    is_occasionselect=false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_occasion array adapter end


        //spinner area array adapter start
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    AreaDropdown areaDropdown = areaDropdownArrayList.get(position);
                    Toast.makeText(ctx,areaDropdown.getArea_name(),Toast.LENGTH_SHORT).show();
                    area = areaDropdown.getArea_name();
                    str_area_id=  areaDropdown.getId();
                    is_areatype=true;
                    /*if(is_pesonselect)
                    {
                        /*rel_lablview.setVisibility(View.VISIBLE);
                        rel_txtview.setVisibility(View.VISIBLE);
                        txt_noseat.setText(str_area+" "+noof_person+" "+"Seats");
                        txt_view_date.setText(day); } */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner area array adapter end
        if (Utils.isNetworkAvailable(ctx)) {
            callapi_getquess(resturant_id);
            callapi_filldropdown(resturant_id);
        }
        else{
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
        }


        txtnextview.setOnClickListener(v -> {
              //testing purpose
              final Intent mainIntent = new Intent(ctx, Walkin_ActionPage.class);
              mainIntent.putExtra("quee_no",quee_no);
              mainIntent.putExtra("person",noof_person);
              mainIntent.putExtra("occasion",occasion);
              mainIntent.putExtra("area",area);
              mainIntent.putExtra("restromodel", restoData);


              startActivity(mainIntent);
        });

        get_inquee.setOnClickListener(v -> {
           //testing purpose hard coded
            identifier="";
            noof_person="8";
            occasion="Birthday";
            area="Terace View";
            String  id = str_area_id;
            //callapi_getqueeconform(action,resturant_id, noof_person, occasion, str_area,identifier);
            if (Utils.isNetworkAvailable(ctx)) {
                callapi_getquee(resturant_id,noof_person,occasion,area);
            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
            }


        });
        //
    }


    @Override
    public void onResume() {
        super.onResume();
        // ((Booking_TabView_Activity)getActivity()).hide_search();
        String service_id = "2";
        /*saveLatitude = 21.209589;
        saveLongitude = 72.860824;*/
        saveLatitude = 23.933689;
        saveLongitude = 72.367458;

        if (Utils.isNetworkAvailable(ctx)) {
            callapi_getbooktable(service_id, String.valueOf(saveLatitude), String.valueOf(saveLongitude));
        }
        else{
            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
        }
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

                                        walkin_listing_adapter = new Walkin_listing_Adapter(Walkin_detail_Fragment.this, restoDataArrayList, "detail",ctx);
                                        recyclerView.setAdapter(walkin_listing_adapter);

                                    }
                                    else{
                                        //no data in array list
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ctx, Constant.NODATA, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    //Api code for get queue list no
    public void callapi_getquess(String resturant_id)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getqueelist("Bearer "+storePrefrence.getString(TOKEN_LOGIN),resturant_id).
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
                                    //Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    quee_no=jsonObject.getString("data");
                                    if(txt_queeno.getText().toString().equals("0"))
                                    {
                                        //set quee_no
                                        txt_queeno.setText(quee_no);
                                    }
                                    else if(Integer.parseInt(txt_queeno.getText().toString()) < Integer.parseInt(quee_no))
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        showAlertView(quee_no);
                                    }
                                    else if(Integer.parseInt(txt_queeno.getText().toString()) > Integer.parseInt(quee_no))
                                    {
                                        txt_queeno.setText(quee_no);
                                    }
                                    else
                                    {
                                        txt_queeno.setText(quee_no);
                                    }
                                }
                                else{
                                    // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
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
                       // Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    //Api code for get person queue  no
    public void callapi_personqueue_no(String resturant_id)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getpersonqueeno("Bearer "+storePrefrence.getString(TOKEN_LOGIN),resturant_id).
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
                                    relative_bottom.setVisibility(View.VISIBLE);
                                    Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    txtqno_bottom.setText("No in current queue: "+ jsonObject.getJSONObject("data").getString("queue_id"));

                                }
                                else{
                                    relative_bottom.setVisibility(View.GONE);
                                    // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                relative_bottom.setVisibility(View.GONE);
                                // Toast.makeText(getContext(), Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }

                            progressBar.setVisibility(View.GONE);
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
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    public  void showAlertView(String quee_no)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.quee_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        TextView txt_msg, tvyes,tvno;

        txt_msg = dialogView.findViewById(R.id.txt_msg);
        tvyes=dialogView.findViewById(R.id.tvyes);
        tvno=dialogView.findViewById(R.id.tvno);
        txt_msg.setText("new queue no is grater than previous quee no still you want to set new queue no ?");

        tvyes.setOnClickListener(v -> {
            dialog.dismiss();
            txt_queeno.setText(quee_no);
        });
        tvno.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }


    public  void showAlertView_yeswantbook(String msg)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.quee_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        TextView txt_msg, tvyes,tvno;

        txt_msg = dialogView.findViewById(R.id.txt_msg);
        tvyes=dialogView.findViewById(R.id.tvyes);
        tvno=dialogView.findViewById(R.id.tvno);
        txt_msg.setText(msg);

        tvyes.setOnClickListener(v -> {
            dialog.dismiss();

            if (Utils.isNetworkAvailable(ctx)) {
                callapi_getqueeconform("yes", restoData.getId(),noof_person,occasion,area, identifier);
            }
            else{
                Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
            }




            //txt_queeno.setText(quee_no);

        });
        tvno.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    private void callapi_filldropdown(String restaurant_id)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getres_detail(restaurant_id).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());
                                if(jsonObject.getString("status").equalsIgnoreCase(SUCCESS_CODE))
                                {
                                    if(jsonObject.getJSONObject("data").getJSONArray("data").length() > 0)
                                    {
                                        areaDropdownArrayList=new ArrayList<>();
                                        for(int i =0; i<jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                        {
                                            //Area Type Spinner code
                                            JSONArray mjson_array_area = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i).getJSONArray("area");
                                            AreaDropdown areaDropdown_first=new AreaDropdown();
                                            areaDropdown_first.setId("0");
                                            areaDropdown_first.setArea_name("Select Area");
                                            areaDropdownArrayList.add(areaDropdown_first);

                                            for(int j = 0; j<mjson_array_area.length(); j++)
                                            {
                                                AreaDropdown areaDropdown=new AreaDropdown();
                                                JSONObject mjson_object_area = mjson_array_area.getJSONObject(j);
                                                areaDropdown.setId(mjson_object_area.getString("id"));
                                                areaDropdown.setArea_name(mjson_object_area.getString("name"));
                                                areaDropdownArrayList.add(areaDropdown);

                                            }
                                            SpinnnerAdapter_Type spinnnerAdapter_type=new SpinnnerAdapter_Type(getApplicationContext(),areaDropdownArrayList);
                                            spinner_type.setAdapter(spinnnerAdapter_type);
                                            //code end
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        //no data in array list
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(ctx, Constant.NODATA, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void callapi_getqueeconform(String action, String restaurant_id, String person, String occasion,String area,String identifier)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().queue_confirmation("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                                        action,restaurant_id, person, occasion, area,identifier).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                //Log.d("Result", jsonObject.toString());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                if (Utils.isNetworkAvailable(ctx)) {
                                    callapi_personqueue_no(restaurant_id);
                                }
                                else{
                                    Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                 progressBar.setVisibility(View.GONE);
                                 JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                 Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                            }

                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void callapi_getquee(String restaurant_id,String person, String occasion,String area)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().queue_get("Bearer "+storePrefrence.getString(TOKEN_LOGIN),
                              restaurant_id, person, occasion, area).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if (jsonObject.has("data") && !jsonObject.isNull("data"))
                                {
                                    // Do something with object.
                                    boolean is_showalert = jsonObject.getJSONObject("data").getBoolean("showalert");
                                    if(is_showalert)
                                    {
                                        showAlertView_yeswantbook(jsonObject.getString("message"));
                                    }
                                    else{
                                        Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    if (Utils.isNetworkAvailable(ctx)) {
                                        callapi_personqueue_no(restaurant_id);
                                    }
                                    else{
                                        Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                                    }
                                }
                           }
                            else if(response.code() == Constant.ERROR_CODE_n)
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                if (Utils.isNetworkAvailable(ctx)) {
                                    callapi_personqueue_no(restaurant_id);
                                }
                                else{
                                    Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                                }
                               /* if (jsonObject.has("data") && !jsonObject.isNull("data"))
                                {
                                    // Do something with object.
                                    boolean is_showalert = jsonObject.getJSONObject("data").getBoolean("showalert");
                                    if(is_showalert)
                                    {
                                        showAlertView_yeswantbook(jsonObject.getString("message"));
                                    }
                                    else{
                                        Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                }*/
                             }
                            else if(response.code() == Constant.ERROR_CODE)
                            {
                                progressBar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ctx, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException | IOException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, Constant.ERRORMSG, Toast.LENGTH_LONG).show();
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
