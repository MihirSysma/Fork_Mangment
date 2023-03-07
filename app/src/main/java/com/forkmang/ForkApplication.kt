package com.forkmang

import android.app.Application
import com.facebook.stetho.Stetho

class ForkApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}