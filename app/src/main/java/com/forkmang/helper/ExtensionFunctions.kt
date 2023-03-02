package com.forkmang.helper

import android.content.Context
import android.util.Log
import android.widget.Toast

// This file stores all the extension functions to make code concise
/**
 * Function used to show toast message: [message] Toast.LENGTH_LONG
 */
fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Function used to Log.i [message] with tag as [tag] which has a default value of SYSMA_TECH
 */
fun Any.logThis(message: String, tag: String = "FORK_TECH") {
    Log.i(tag, message)
}

/**
 * Function used to Error log Log.e [message] with tag as [tag] which has a default value of SYSMA_TECH
 */
fun Any.errorLogThis(message: String, tag: String = "FORK_TECH") {
    Log.e(tag, message)
}