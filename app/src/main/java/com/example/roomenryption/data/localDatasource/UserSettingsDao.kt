package com.example.roomenryption.data.localDatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userSettings: UserSettingsEntity)

    @Query("SELECT * FROM user_settings where id = 0")
    suspend fun getUserSettings(): UserSettingsEntity // Changed return type
}