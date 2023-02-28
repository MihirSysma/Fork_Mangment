package com.forkmang.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.forkmang.R
import java.io.IOException
import java.util.*

object ApiConfig {
    private const val PERMISSION_CALLBACK_CONSTANT = 100
    private const val REQUEST_PERMISSION_SETTING = 101
    var permissionsRequired = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA
    )
    var gps: GPSTracker? = null
    var user_location: String? = ""
    var latitude1 = 0.0
    var longitude1 = 0.0
    fun getAddress(lat: Double, lng: Double, activity: Activity?): String {
        val geocoder = Geocoder(activity!!, Locale.getDefault())
        var address = ""
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses!!.size != 0) {
                val obj = addresses[0]
                val add = obj.getAddressLine(0)
                address = add
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
        }
        return address
    }

    @JvmStatic
    fun getLocation(activity: Activity) {
        try {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permissionsRequired[0]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    permissionsRequired[0]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    permissionsRequired[2]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    permissionsRequired[3]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    permissionsRequired[4]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsRequired[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsRequired[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsRequired[2]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsRequired[3]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsRequired[4]
                    )
                ) {
                    // Show an explanation to the user asynchronously -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    AlertDialog.Builder(activity)
                        .setTitle(activity.resources.getString(R.string.location_permission))
                        .setMessage(activity.resources.getString(R.string.location_permission_message))
                        .setPositiveButton("Ok") { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.CALL_PHONE
                                ),
                                0
                            )
                            Constant.is_permission_grant = 1
                        }
                        .create()
                        .show()
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        0
                    )
                }
            } else {
                gps = GPSTracker(activity)
                if (gps!!.canGetLocation()) {
                    user_location = gps!!.getAddressLine(activity)
                }
                if (gps?.isGPSTrackingEnabled == true) {
                    latitude1 = gps!!.latitude
                    longitude1 = gps!!.longitude
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isGPSEnable(activity: Activity): Boolean {
        val locationManager =
            (activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}