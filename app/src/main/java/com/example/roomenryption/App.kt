package com.example.roomenryption

import android.app.Application
import com.example.roomenryption.data.localDatasource.DatabaseProvider
import dagger.hilt.android.HiltAndroidApp

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.initialize(this)
    }
}