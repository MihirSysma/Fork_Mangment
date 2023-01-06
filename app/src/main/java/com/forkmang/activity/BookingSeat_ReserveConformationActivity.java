package com.forkmang.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.models.BookTable;
import com.forkmang.models.TableList;

public class BookingSeat_ReserveConformationActivity extends AppCompatActivity {

    TableList tableList_get;
    BookTable bookTable;
    RecyclerView recyclerView;
    TextView txt_totalPaidamt,txt_rule,txt_dresscode,txt_customername,txt_mobileno,txt_customer_add,txt_timeview,txt_indoor;
    TextView txtrestroname, txt_endtime, txt_distance,txttotalkm;
    Context ctx = BookingSeat_ReserveConformationActivity.this;
    StorePrefrence storePrefrence;
    String totalpay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_seatreserve);
        storePrefrence = new StorePrefrence(ctx);
        txt_totalPaidamt = findViewById(R.id.txt_totalPaidamt);
        txtrestroname = findViewById(R.id.txt_hotelname);
        txt_endtime = findViewById(R.id.txt_endtime);
        txt_distance = findViewById(R.id.txt_distance);
        txttotalkm = findViewById(R.id.txt_totalkm);
        txt_timeview = findViewById(R.id.txt_timeview);
        txt_indoor = findViewById(R.id.txt_indoor);
        txt_customername = findViewById(R.id.txt_customername);
        txt_customer_add = findViewById(R.id.txt_customer_add);
        txt_mobileno = findViewById(R.id.txt_mobileno);
        txt_rule = findViewById(R.id.txt_rule);
        txt_dresscode = findViewById(R.id.txt_dresscode);


        tableList_get = (TableList) getIntent().getSerializableExtra("model");
        bookTable = (BookTable) getIntent().getSerializableExtra("bookTable");
        totalpay = getIntent().getStringExtra("totalpay");

        txtrestroname.setText(bookTable.getRest_name());
        txt_endtime.setText(bookTable.getEndtime());
        txttotalkm.setText(bookTable.getDistance()+" Km");

        //String data_total = Select_Food_Fragment.cartBookingArrayList.get(0).getData_total();
        txt_totalPaidamt.setText(totalpay);

        txt_rule.setText(tableList_get.getTable_rule());
        txt_dresscode.setText(tableList_get.getTable_drescode());
        txt_timeview.setText(tableList_get.getStr_time());
        txt_customername.setText(tableList_get.getStr_customer_name());
        txt_mobileno.setText(storePrefrence.getString(Constant.MOBILE));
        txt_indoor.setText(tableList_get.getNumber_of_person() + " " + "Seats");

    }
}