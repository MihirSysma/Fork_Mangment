package com.forkmang.activity;

import static com.forkmang.helper.Constant.SUCCESS_CODE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.adapter.ListTableBookingAdapter;
import com.forkmang.adapter.SpinnnerAdapter;
import com.forkmang.adapter.SpinnnerAdapter_Branch;
import com.forkmang.adapter.SpinnnerAdapter_Floor;
import com.forkmang.adapter.SpinnnerAdapter_Type;
import com.forkmang.adapter.SpinnnerAdapter_Type_Value;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;
import com.forkmang.network_call.Api;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingTable_DetailView extends Activity {

    Context ctx = BookingTable_DetailView.this;
    int mYear, mMonth, mDay, mHour, mMinute;
    String booking_date, booking_time, Date_get="",resturant_id, noof_person="0";
    ArrayList<TableList> tableListArrayList;
    FrameLayout frame_layout;
    LinearLayout linear_layout_under_frame,linear_listview,lyt_datetime;;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView txt_view_datetime;
    RadioButton button_floor,button_list;
    BookTable bookTable;
    StorePrefrence storePrefrence;


    String [] person =
            {"Select Person","1","2 ","3","4","5","6","7","8","9","10"};
    String [] branch =
            {"Select Branch","1","2 ","3","4","5"};
    String [] floor =
            {"Select Floor","1","2","3","4","5"};
    String [] type =
            {"Select Type","inside","outside"};
    String [] type_value =
            {"Value","0.7","0.8","0.9","1.0"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingtable_detailview);
        storePrefrence = new StorePrefrence(ctx);
        Intent intent = getIntent();
        resturant_id = intent.getStringExtra("resturant_id");
        bookTable = (BookTable) getIntent().getSerializableExtra("model");

        TextView txtrestroname = findViewById(R.id.txtrestroname);
        txt_view_datetime = findViewById(R.id.txt_datetime);
        TextView txt_time = findViewById(R.id.txt_time);
        TextView txt_distance = findViewById(R.id.txt_distance);
        TextView txt_totalkm = findViewById(R.id.txt_totalkm);
        button_floor = findViewById(R.id.button_floor);
        button_list = findViewById(R.id.button_list);
        frame_layout = findViewById(R.id.frame_layout);
        linear_layout_under_frame = findViewById(R.id.linear_layout);
        lyt_datetime= findViewById(R.id.lyt_datetime);
        Button btn_payment_conform = findViewById(R.id.btn_payment_conform);
        linear_listview = findViewById(R.id.linear_listview);
        Spinner spinner_person= findViewById(R.id.spinner);
        Spinner spinner_branch= findViewById(R.id.spinner_branch);
        Spinner spinner_floor= findViewById(R.id.spinner_floor);
        Spinner spinner_type= findViewById(R.id.spinner_type);
        Spinner spinner_type_value= findViewById(R.id.spinner_type_value);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.table_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookingTable_DetailView.this, LinearLayoutManager.HORIZONTAL, false));

        button_floor.setChecked(false);
        button_list.setChecked(true);

        txtrestroname.setText(bookTable.getRest_name());
        txt_time.setText(bookTable.getEndtime());
        txt_totalkm.setText(bookTable.getDistance()+" km");

        current_dateshow();



        btn_payment_conform.setOnClickListener(v -> {

        });

        button_floor.setOnClickListener(v -> {
            frame_layout.setVisibility(View.VISIBLE);
            linear_layout_under_frame.setVisibility(View.VISIBLE);
            linear_listview.setVisibility(View.GONE);
        });

        button_list.setOnClickListener(v -> {
            linear_listview.setVisibility(View.VISIBLE);

            frame_layout.setVisibility(View.GONE);
            linear_layout_under_frame.setVisibility(View.GONE);
            recyclerView = findViewById(R.id.table_recycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(BookingTable_DetailView.this, LinearLayoutManager.HORIZONTAL, false));
            callapi_booktablelist(resturant_id);
        });

        lyt_datetime.setOnClickListener(v -> datePicker());

        //spinner_person array adapter start
        SpinnnerAdapter personAdapter=new SpinnnerAdapter(getApplicationContext(),person);
        spinner_person.setAdapter(personAdapter);
        spinner_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    Toast.makeText(ctx,person[position],Toast.LENGTH_SHORT).show();
                    noof_person = person[position];
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_person array adapter end


        //spinner_branch array adapter
         SpinnnerAdapter_Branch branchAdapter=new SpinnnerAdapter_Branch(getApplicationContext(),branch);
         spinner_branch.setAdapter(branchAdapter);
         spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    Toast.makeText(ctx,branch[position],Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_branch array adapter

        //spinner_floor array adapter
         SpinnnerAdapter_Floor floorAdapter=new SpinnnerAdapter_Floor(getApplicationContext(),floor);
         spinner_floor.setAdapter(floorAdapter);
         spinner_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    Toast.makeText(ctx,floor[position],Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_floor array adapter

        //spinner_floor array adapter
        SpinnnerAdapter_Type typeAdapter=new SpinnnerAdapter_Type(getApplicationContext(),type);
        spinner_type.setAdapter(typeAdapter);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    Toast.makeText(ctx,type[position],Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_floor array adapter

        //spinner_type array adapter
        SpinnnerAdapter_Type_Value type_valueAdapter=new SpinnnerAdapter_Type_Value(getApplicationContext(),type_value);
        spinner_type_value.setAdapter(type_valueAdapter);
        spinner_type_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    Toast.makeText(ctx,type_value[position],Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(ctx,"not selected",Toast.LENGTH_SHORT).show();
            }
        });
        //spinner_floor array adapter

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(button_list.isChecked())
        {
            linear_listview.setVisibility(View.VISIBLE);
            frame_layout.setVisibility(View.GONE);
            linear_layout_under_frame.setVisibility(View.GONE);

            callapi_booktablelist(resturant_id);
        }
    }

    public void showAlertView_tableselctionrule(TableList tableList)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTable_DetailView.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.tableselection_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_select_nxt;
        TextView tv_descr, tv_rule, tv_dresscode, tv_ocassion;
        btn_select_nxt=dialogView.findViewById(R.id.btn_select_nxt);

        tv_descr=dialogView.findViewById(R.id.tv_descr);
        tv_rule=dialogView.findViewById(R.id.tv_rule);
        tv_dresscode=dialogView.findViewById(R.id.tv_dresscode);
        tv_ocassion=dialogView.findViewById(R.id.tv_ocassion);

        tv_descr.setText(tableList.getTable_descr());
        tv_rule.setText(tableList.getTable_rule());
        tv_dresscode.setText(tableList.getTable_drescode());
        tv_ocassion.setText(tableList.getTable_ocassion());


        btn_select_nxt.setOnClickListener(v -> {
            dialog.dismiss();
            showAlertView_paymentconform(tableList);
        });
        dialog.show();

    }

    private void showAlertView_paymentconform(TableList tableList)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTable_DetailView.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.payment_conform_alertview, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button btn_cancel,btn_cnf_payment,btn_select_food;
        TextView txt_restroname, txt_custname, txt_datetime,txt_phoneno;
        EditText etv_noperson;
        ImageView imgicon_edit,imgicon_save;

        txt_restroname =dialogView.findViewById(R.id.txt_restroname);
        txt_custname =dialogView.findViewById(R.id.txt_custname);
        txt_datetime =dialogView.findViewById(R.id.txt_datetime);
        txt_phoneno =dialogView.findViewById(R.id.txt_phoneno);
        etv_noperson =dialogView.findViewById(R.id.etv_noperson);
        imgicon_edit =dialogView.findViewById(R.id.imgicon_edit);
        imgicon_save =dialogView.findViewById(R.id.imgicon_save);
        txt_restroname.setText(tableList.getStr_hotel_name());
        txt_custname.setText(tableList.getStr_customer_name());
        txt_datetime.setText(tableList.getStr_time());

        etv_noperson.setText(tableList.getNumber_of_person() );

        txt_phoneno.setText(tableList.getStr_phone());
        btn_select_food=dialogView.findViewById(R.id.btn_select_food);
        btn_cancel=dialogView.findViewById(R.id.btn_cancel);



        imgicon_edit.setOnClickListener(v -> {
            imgicon_save.setVisibility(View.VISIBLE);
            imgicon_edit.setVisibility(View.GONE);
            etv_noperson.setEnabled(true);
            etv_noperson.setBackgroundColor(Color.DKGRAY);
            etv_noperson.setTextColor(Color.WHITE);
            /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);*/
        });

        imgicon_save.setOnClickListener(v -> {
            if(etv_noperson.getText().length() > 0 && !etv_noperson.getText().toString().equalsIgnoreCase("0"))
            {
                imgicon_save.setVisibility(View.GONE);
                imgicon_edit.setVisibility(View.VISIBLE);
                etv_noperson.setEnabled(false);

                etv_noperson.setBackgroundColor(Color.WHITE);
                etv_noperson.setTextColor(Color.parseColor("#ADACAC"));
            }
            else{
                Toast.makeText(ctx,"No of person can't be empty or zero", Toast.LENGTH_SHORT).show();
            }



        });



        btn_cnf_payment=dialogView.findViewById(R.id.btn_cnf_payment);

        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        btn_cnf_payment.setOnClickListener(v ->{

                showAlertView_conformtable();
                dialog.dismiss();

        });

        btn_select_food.setOnClickListener(v -> {
            final Intent mainIntent = new Intent(BookingTable_DetailView.this, BookingTable_ReserveSeat.class);
            startActivity(mainIntent);
            dialog.dismiss();
        });

        dialog.show();

    }

    private void showAlertView_conformtable()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingTable_DetailView.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablereserve_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();

        dialog.show();

    }

    private void callapi_booktablelist(String restaurant_id)
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
                                        tableListArrayList = new ArrayList<>();
                                        for(int i =0; i<jsonObject.getJSONObject("data").getJSONArray("data").length(); i++)
                                        {
                                            JSONArray mjson_array = jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(i).getJSONArray("table");
                                            for(int j = 0; j<mjson_array.length(); j++)
                                            {
                                                JSONObject mjson_object = mjson_array.getJSONObject(j);
                                                TableList tableList = new TableList();
                                                tableList.setId(mjson_object.getString("id"));
                                                tableList.setRestaurant_id(mjson_object.getString("restaurant_id"));
                                                tableList.setTable_no(mjson_object.getString("table_no"));
                                                tableList.setNumber_of_person(mjson_object.getString("number_of_person"));
                                                tableList.setType(mjson_object.getString("type"));
                                                tableList.setStatus_id(mjson_object.getString("status_id"));

                                                //tableList.setFloor_id(mjson_object.getString("floor_id"));
                                                //tableList.setArea_id(mjson_object.getString("area_id"));
                                                tableList.setPrice(mjson_object.getString("price"));

                                                /* parameter for table reservation not available now  */

                                                if(mjson_object.has("table_descr"))
                                                {
                                                    tableList.setTable_descr(mjson_object.getString("table_descr"));
                                                }
                                                else{
                                                    tableList.setTable_descr("Test description for table...");
                                                }

                                                if(mjson_object.has("table_rule"))
                                                {
                                                    tableList.setTable_rule(mjson_object.getString("table_rule"));
                                                }
                                                else{
                                                    tableList.setTable_rule("Test rule for table");
                                                }

                                                if(mjson_object.has("table_drescode"))
                                                {
                                                    tableList.setTable_drescode(mjson_object.getString("table_drescode"));
                                                }
                                                else{
                                                    tableList.setTable_drescode("White Shirt and Blue Jeans");
                                                }

                                                if(mjson_object.has("table_ocassion"))
                                                {
                                                    tableList.setTable_ocassion(mjson_object.getString("table_ocassion"));
                                                }
                                                else{
                                                    tableList.setTable_ocassion("Birthday Bash");
                                                }
                                                /* code end for table reservation */



                                                /* code to get customer and other data into table object */
                                                tableList.setStr_hotel_name(bookTable.getRest_name());

                                                if(storePrefrence.getString(Constant.NAME).length() == 0)
                                                {
                                                    tableList.setStr_customer_name("Test customer name");
                                                }
                                                else{
                                                    tableList.setStr_customer_name(storePrefrence.getString(Constant.NAME));
                                                }

                                                //tableList.setStr_noseat(noof_person+" "+"Seats");
                                                tableList.setStr_time(txt_view_datetime.getText().toString());

                                                if(storePrefrence.getString(Constant.MOBILE).length() == 0)
                                                {
                                                    tableList.setStr_phone("9000012345");
                                                }
                                                else{
                                                    tableList.setStr_phone(storePrefrence.getString(Constant.MOBILE));
                                                }

                                                /* code to get customer and other data into table object end */

                                                tableListArrayList.add(tableList);
                                            }
                                        }

                                        progressBar.setVisibility(View.GONE);
                                        //go to adapter
                                        ListTableBookingAdapter listTableBookingAdapter = new ListTableBookingAdapter(BookingTable_DetailView.this, tableListArrayList);
                                        recyclerView.setAdapter(listTableBookingAdapter);
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

    //Date and time picker example code start
    private void datePicker()
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // when dialog box is closed, below method will be called.
        final DatePickerDialog.OnDateSetListener datePickerListener = (view, selectedYear, selectedMonth, selectedDay) -> {
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
            txt_view_datetime.setText(selectedDay + "-" +getMonth(selectedMonth + 1 ));
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                ctx, R.style.DialogTheme_picker,datePickerListener,
                mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel),
                (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialog.cancel();
                    }
                });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE)
                        {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,R.style.DialogTheme_picker,
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


    private void current_dateshow()
    {
        final Calendar c = Calendar.getInstance();
        String str_month="",str_date="", str_day,selectedYear,booking_date_n;

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        String AM_PM ;
        if(mHour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        String time = mHour + ":" + mMinute +" "+ AM_PM;


        int month_n =  mMonth + 1;
        int date_n = mDay;

        str_day = String.valueOf(date_n);

        if(month_n < 10 )
        {
            str_month =  "0"+month_n;
        }
        else{
            str_month =  String.valueOf(month_n);

        }

        if(date_n < 10)
        {
            str_date =  "0"+ date_n;
        }
        else{
            str_date =  String.valueOf(date_n);
        }

        selectedYear = String.valueOf(mYear);
        booking_date = selectedYear + "-" + str_month + "-" + str_date;

        txt_view_datetime.setText("");
        txt_view_datetime.setText(str_day + "-" +getMonth(month_n) + " " +time);
    }


    private String getMonth(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month-1];
    }

    //Date and time picker example code end



}
