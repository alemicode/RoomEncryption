package com.example.roomenryption.data.localDatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val password: String,
)