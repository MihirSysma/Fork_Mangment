package com.forkmang.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.Serializable

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

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}