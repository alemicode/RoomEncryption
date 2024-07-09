package com.example.roomenryption.data.localDatasource

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    lateinit var db: AppDatabase

    fun initialize(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "user-settings-db"
        ).build()
    }
}