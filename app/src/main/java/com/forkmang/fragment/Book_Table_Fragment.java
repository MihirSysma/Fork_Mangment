package com.forkmang.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.BookTableAdapter;
import com.forkmang.helper.ApiConfig;
import com.forkmang.helper.GPSTracker;
import com.forkmang.models.BookTable;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Book_Table_Fragment extends Fragment {
    ArrayList<BookTable> bookTableArrayList;
    RecyclerView recyclerView;
    ImageView date_icon, time_icon;
    TextView txt_view_datetime,txt_person;
    GPSTracker gps;
    Double saveLatitude,saveLongitude;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String booking_date, booking_time, Date_get="";
    boolean isOkayClicked;
    LinearLayout lyt_msgview, lyt_datetime;
    Spinner spinner;
    String noof_person="", search_str="";
    ProgressBar progressBar;

    String [] person =
            {"Select Person","Person 1","Person 2 ","Person 3","Person 4","Person 5","Person 6","Person 7","Person 8","Person 9","Person 10"};

   public static Book_Table_Fragment newInstance() {
        return new Book_Table_Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booktable_layout, container, false);
        recyclerView = view.findViewById(R.id.booktable_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        date_icon = view.findViewById(R.id.date_icon);
        txt_person= view.findViewById(R.id.txt_person);
        spinner= view.findViewById(R.id.spinner);
        time_icon = view.findViewById(R.id.time_icon);
        txt_view_datetime = view.findViewById(R.id.txt_datetime);
        lyt_msgview= view.findViewById(R.id.lyt_msgview);
        lyt_datetime= view.findViewById(R.id.lyt_datetime);
        progressBar= view.findViewById(R.id.progressBar);

        lyt_msgview.setVisibility(View.VISIBLE);
        lyt_datetime.setVisibility(View.GONE);

        ApiConfig.getLocation(getActivity());
        gps = new GPSTracker(getActivity());
        saveLatitude = gps.latitude;
        saveLongitude = gps.longitude;


        if (gps.getIsGPSTrackingEnabled())
        {
            saveLatitude = gps.latitude;
            saveLongitude = gps.longitude;
        }

        String service_id = "2";
        saveLatitude = 23.933689;
        saveLongitude = 72.367458;


        //spinner array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, person);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getContext(),person[position],Toast.LENGTH_SHORT).show();

                String str = person[position];
                if(str.equalsIgnoreCase("Select Person"))
                {
                    //nothing do
                    noof_person="0";
                }
                else{
                    String[] strdate_arr_2 = str.split(" ");
                    noof_person = strdate_arr_2[1];

                    //call api for filter



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"not selected",Toast.LENGTH_SHORT).show();
                if(noof_person.equals("0"))
                {
                     //nothing selected
                }


            }
        });
        //spinner array adapter

        callapi_getbooktable(service_id, saveLatitude.toString(), saveLongitude.toString());


        lyt_msgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });


        lyt_datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        /*date_icon.setOnClickListener(view1 -> datePicker());

        time_icon.setOnClickListener(v -> {
            timePicker();


        });*/

        return view;

    }

    private void datePicker()
    {
        lyt_msgview.setVisibility(View.GONE);
        lyt_datetime.setVisibility(View.VISIBLE);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // when dialog box is closed, below method will be called.
        final DatePickerDialog.OnDateSetListener datePickerListener = (view, selectedYear, selectedMonth, selectedDay) -> {
            isOkayClicked=true;
            if (isOkayClicked)
            {
                int month =  selectedMonth + 1;
                int date = selectedDay;
                String str_month="",str_date="";
                if(month < 10 )
                {
                    str_month =  "0"+month;
                }
                else{
                    str_month =  String.valueOf(month);
                }

                if(date < 10)
                {
                    str_date =  "0"+ date;
                }
                else{
                    str_date =  String.valueOf(date);
                }

                booking_date = selectedYear + "-" + str_month + "-" + str_date;
                Log.d("sendate==>", booking_date);

                txt_view_datetime.setText("");
                /*txt_view_datetime.setText(selectedDay + "-" +getMonth(selectedMonth + 1 ) + "-"
                        + selectedYear);*/
                txt_view_datetime.setText(selectedDay + "-" +getMonth(selectedMonth + 1 ));
                mYear = selectedYear;
                mMonth = selectedMonth;
                mDay = selectedDay;
            }
            isOkayClicked = false;
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), TimePickerDialog.THEME_HOLO_LIGHT,datePickerListener,
                mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                            if(Date_get.length()==0)
                            {
                                lyt_msgview.setVisibility(View.VISIBLE);
                                lyt_datetime.setVisibility(View.GONE);
                            }
                            else{
                                lyt_msgview.setVisibility(View.GONE);
                                lyt_datetime.setVisibility(View.VISIBLE);
                            }
                            isOkayClicked = false;
                        }
                    }
                });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            isOkayClicked = true;
                            DatePicker datePicker = datePickerDialog
                                    .getDatePicker();

                            datePickerListener.onDateSet(datePicker,
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth());

                            int month = datePicker.getMonth();
                            int date = datePicker.getDayOfMonth();

                            String str_month="", str_date="";
                            month =  month + 1;
                            if(month < 10 )
                            {
                                str_month =  "0"+month;
                            }
                            else{
                                str_month =  String.valueOf(month);
                            }

                            if(date < 10)
                            {
                                str_date =  "0"+ date;
                            }
                            else{
                                str_date =  String.valueOf(date);
                            }

                            Date_get = datePicker.getYear() + "-" + str_month  + "-" + str_date;
                            Log.d("sendate==>", Date_get);
                            timePicker();


                            //callApi_senddate(deliveryDate_get);

                        }
                    }
                });

        datePickerDialog.setCancelable(false);
        long now = System.currentTimeMillis() ;
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        /*if(Constant.DELIVERY_MAXDATE_AFTER_ORDER == 0)
        {
            //user can select any date
        }
        else if(Constant.DELIVERY_MAXDATE_AFTER_ORDER > 0){
            datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*Constant.DELIVERY_MAXDATE_AFTER_ORDER));
        }
       */
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }

    private void timePicker()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),TimePickerDialog.THEME_HOLO_LIGHT,
                (view, hourOfDay, minute) -> {
                    String date = txt_view_datetime.getText().toString();
                    txt_view_datetime.setText("");

                    String time = hourOfDay + ":" + minute;


                    String AM_PM ;
                    if(hourOfDay < 12) {
                        AM_PM = "AM";
                    } else {
                        AM_PM = "PM";
                    }

                    booking_date = booking_date + " "+ time + " "+AM_PM;
                    Log.d("senddate", booking_date);

                    txt_view_datetime.setText(date+", "+ time+" "+AM_PM);
                }, mHour, mMinute, false);

        timePickerDialog.show();
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    private String getMonth(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month-1];
    }


    private void callapi_getbooktable(String service_id, String latitude, String logitutde)
    {
        progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().getlist_res(service_id, latitude, logitutde).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Result", jsonObject.toString());

                            if(jsonObject.getString("status").equalsIgnoreCase("Success"))
                            {
                                if(jsonObject.getJSONObject("data").getJSONArray("data").length() > 0)
                                {
                                    bookTableArrayList = new ArrayList<>();
                                    for(int i =0; i<jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                    {
                                        BookTable bookTable = new BookTable();

                                        JSONObject mjson_obj = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i);
                                        bookTable.setRest_name(mjson_obj.getString("rest_name"));
                                        bookTable.setEndtime(mjson_obj.getString("endtime"));

                                        double double_val = Math.floor(mjson_obj.getDouble("distance") * 100) / 100;
                                        bookTable.setDistance(Double.toString(double_val));

                                        bookTableArrayList.add(bookTable);
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    //go to adapter
                                    BookTableAdapter bookTableAdapter = new BookTableAdapter(getActivity(),bookTableArrayList);
                                    recyclerView.setAdapter(bookTableAdapter);

                                }
                                else{
                                    //no data in array list
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                           // progressBar.setVisibility(View.GONE);

                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), "Error occur please try again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }




}
