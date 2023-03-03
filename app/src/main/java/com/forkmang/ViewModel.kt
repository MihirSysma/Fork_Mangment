package com.forkmang

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ViewModel(var app: Application) : AndroidViewModel(app) {

    val searchData = MutableLiveData<String>()
}