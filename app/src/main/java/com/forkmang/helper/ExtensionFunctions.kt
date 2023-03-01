package com.forkmang.helper

import android.content.Context
import android.widget.Toast

// This file stores all the extension functions to make code concise
/**
 * Function used to show toast message: [message] Toast.LENGTH_LONG
 */
fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}