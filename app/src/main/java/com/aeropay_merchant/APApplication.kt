package com.aeropay_merchant

import android.app.Application
import com.google.firebase.FirebaseOptions

class APApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        var options =  FirebaseOptions.Builder()
            .setApiKey("AIzaSyBy5X8s5FMClItX4FzhuzfeZX7LkRgpH3U")
            .setApplicationId("1:812992319148:android:65840eb18b72b4ca")
            .build()
    }
}