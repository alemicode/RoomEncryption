package com.example.roomenryption

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomenryption.data.localDatasource.DatabaseProvider
import com.example.roomenryption.data.localDatasource.UserSettingsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val userSettingsDao = DatabaseProvider.db.userSettingsDao()
    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState: StateFlow<UserSettingsUiState> = _uiState.asStateFlow()


    fun saveUserSettings(username: String, password: String) {
        viewModelScope.launch {
            val encryptedUsername = CryptoUtils.encrypt(username)
            val encryptedPassword = CryptoUtils.encrypt(password)

            userSettingsDao.insert(
                UserSettingsEntity(
                    id = 0,
                    username = encryptedUsername,
                    password = encryptedPassword,
                )
            )

            _uiState.value = UserSettingsUiState(
                username_encryption = encryptedUsername,
                password_encryption = encryptedPassword
            )
        }
    }

    fun loadUserSettings() {
        viewModelScope.launch {
            val userSettings = userSettingsDao.getUserSettings()

            userSettings
            if (userSettings != null) {

                val decryptedUsername = CryptoUtils.decrypt(userSettings.username)
                val decryptedPassword = CryptoUtils.decrypt(userSettings.password)

                _uiState.value = _uiState.value.copy(
                    username = decryptedUsername,
                    password = decryptedPassword
                )
            }
        }
    }
}

data class UserSettingsUiState(
    val username: String = "",
    val password: String = "",
    val username_encryption: String = "",
    val password_encryption: String = ""
)