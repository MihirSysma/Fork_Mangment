package com.forkmang.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.forkmang.R

class FaceLoginPermission : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faceloginpermission)

        val btnEnable: Button = findViewById(R.id.btn_enable)
        btnEnable.setOnClickListener { showAlertView() }
    }

    private fun showAlertView() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@FaceLoginPermission)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(R.layout.conform_tablereserve_view, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog: AlertDialog = alertDialog.create()
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel)
        val btnYes: Button = dialogView.findViewById(R.id.btn_yes)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnYes.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}