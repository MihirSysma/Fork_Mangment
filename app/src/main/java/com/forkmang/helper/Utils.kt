package com.forkmang.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException

object Utils {
    private const val TAG = ""
    private const val cryptoPass = ""
    var App_Code = ""
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        if(Build.VERSION.SDK_INT > 23) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            val isNetworkConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return isNetworkConnected
        }else {
            return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo != null
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
            try {
                locationMode =
                    Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }
}