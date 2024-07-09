package com.example.roomenryption.data.localDatasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserSettingsEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
}