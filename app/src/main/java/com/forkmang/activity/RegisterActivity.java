package com.forkmang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.forkmang.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button Btn_Back = findViewById(R.id.Btn_Back);
        Button btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(v -> {
            showAlertView();
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
            showAlertView_2();
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
        TextView txt_otpenter;
        txt_otpenter=dialogView.findViewById(R.id.txt_otpenter);
        txt_otpenter.setOnClickListener(v -> {
            showAlertView();
            dialog.dismiss();
        });


        Btn_Done=dialogView.findViewById(R.id.btn_done);
        Btn_Done.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }
}