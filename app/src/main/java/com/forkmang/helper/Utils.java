package com.forkmang.helper;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
    private static final String TAG = "";
    private static String cryptoPass = "";
    public static String App_Code = "";

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

}
