package com.forkmang.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.forkmang.R;

public class FaceLoginPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceloginpermission);
        Button Btn_Enable = findViewById(R.id.btn_enable);

        Btn_Enable.setOnClickListener(v -> {
            showAlertView();
        });

    }

    private void showAlertView()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FaceLoginPermission.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.conform_tablereserve_view, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        final AlertDialog dialog = alertDialog.create();
        Button Btn_Cancel,Btn_Yes ;

        Btn_Cancel=dialogView.findViewById(R.id.btn_cancel);
        Btn_Yes=dialogView.findViewById(R.id.btn_yes);

        Btn_Cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Btn_Yes.setOnClickListener(v -> {
            dialog.dismiss();
            /*final Intent mainIntent = new Intent(FaceLoginPermission.this, FaceLogin.class);
            startActivity(mainIntent);
            finish();*/

        });

        dialog.show();

    }


}