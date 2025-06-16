package com.umt.memingoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class Memingo: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}