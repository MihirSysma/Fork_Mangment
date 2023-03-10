package com.forkmang.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.forkmang.adapter.CartListingAdapter
import com.forkmang.databinding.ActivityPaymentSummaryPickupBinding

class ActivityPaymentSummaryPickup : AppCompatActivity() {

    private val binding by lazy { ActivityPaymentSummaryPickupBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recycleview.layoutManager = LinearLayoutManager(this@ActivityPaymentSummaryPickup)
        if (loadLocale().equals("ar", ignoreCase = true)) {
            binding.lytEng.visibility = View.GONE
            binding.lytArabic.visibility = View.VISIBLE
        } else {
            //english
            binding.lytArabic.visibility = View.GONE
            binding.lytEng.visibility = View.VISIBLE
        }
        binding.btnPaymentProceed.setOnClickListener {
            val mainIntent = Intent(
                this@ActivityPaymentSummaryPickup,
                PickupConformationActivity::class.java
            )
            startActivity(mainIntent)
        }
        val cartBookingAdapter = CartListingAdapter(this@ActivityPaymentSummaryPickup)
        binding.recycleview.adapter = cartBookingAdapter
    }

    private fun loadLocale(): String? {
        val langPref = "Language"
        val prefs = getSharedPreferences(
            "CommonPrefs",
            MODE_PRIVATE
        )
        return prefs.getString(langPref, "")
    }
}