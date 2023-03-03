package com.forkmang.helper

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class GPSTracker : Service, LocationListener {
    private var mContext: Context? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    var canGetLocation = false
    var geocoderMaxResults = 1
    var location: Location? = null
    @JvmField
    var latitude // latitude
            = 0.0
    @JvmField
    var longitude // longitude
            = 0.0
    var isGPSTrackingEnabled = false
    protected var locationManager: LocationManager? = null

    constructor() {}
    constructor(context: Context?) {
        mContext = context
        getUserLocation()
    }

    fun getUserLocation(): Location? {
        try {
            locationManager = mContext
                ?.getSystemService(LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager
                ?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            isNetworkEnabled = locationManager
                ?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                canGetLocation = true
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(
                            (mContext as Activity?)!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            (mContext as Activity?)!!, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            (mContext as Activity?)!!, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 0
                        )
                    }
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location?.latitude?:0.0
                            longitude = location?.longitude?:0.0
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager?.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location?.latitude?:0.0
                                longitude = location?.longitude?:0.0
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    fun getGeocoderAddress(context: Context?): List<Address>? {
        if (location != null) {
            val geocoder = Geocoder(context!!, Locale.ENGLISH)
            try {
                return geocoder.getFromLocation(latitude, longitude, geocoderMaxResults)
            } catch (e: IOException) {
                //System.out.println("================ = "+e.getMessage());
            }
        }
        return null
    }

    fun getAddressLine(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.getAddressLine(0)
        } else {
            null
        }
    }

    fun getLocality(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.locality
        } else {
            null
        }
    }

    fun getPostalCode(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.postalCode
        } else {
            null
        }
    }

    fun getCountryName(context: Context?): String? {
        val addresses = getGeocoderAddress(context)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.countryName
        } else {
            null
        }
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager?.removeUpdates(this@GPSTracker)
        }
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location?.latitude?:0.0
        }

        // return latitude
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location?.longitude?:0.0
        }
        return longitude
    }

    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun updateGPSCoordinates() {
        if (location != null) {
            latitude = location?.latitude?:0.0
            longitude = location?.longitude?:0.0
        }
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    }
}