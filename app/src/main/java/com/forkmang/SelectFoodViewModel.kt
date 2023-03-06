package com.forkmang

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SelectFoodViewModel(var app: Application) : AndroidViewModel(app) {

    val command = MutableLiveData<String>()
}