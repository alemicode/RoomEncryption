package com.example.roomenryption

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomenryption.ui.theme.RoomEnryptionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomEnryptionTheme {
                val viewModel: MainViewModel = viewModel()
                val saveUserSettings = viewModel::saveUserSettings
                val loadUserSettings = viewModel::loadUserSettings
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    MainScreen(
                        innerPadding,
                        saveUserSettings,
                        loadUserSettings,
                        uiState,
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    innerPadding: PaddingValues,
    saveUserSettings: (username: String, password: String) -> Unit,
    loadUserSettings: () -> Unit,
    uiState: UserSettingsUiState,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val username = remember {
            mutableStateOf("")
        }
        val passwords = remember {
            mutableStateOf("")
        }
        TextField(
            modifier = Modifier.padding(10.dp),
            value = username.value,

            onValueChange = { username.value = it },
            placeholder = { Text("Your name here") })
        TextField(
            value = passwords.value,
            onValueChange = { passwords.value = it },
            placeholder = { Text("Your password here") })
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { saveUserSettings(username.value, passwords.value) }) {
            Text("Encrypt Data")
        }

        Button(onClick = { loadUserSettings() }) {
            Text("Load data from Room")
        }

        if (!uiState.username_encryption.isNullOrBlank()) {
            Text(
                "encrypted password: ${uiState.username_encryption}",
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                textAlign = TextAlign.Center
            )
            Text(
                "encrypted password: ${uiState.password_encryption}",
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(100.dp),
                textAlign = TextAlign.Center

            )
        }

        if (!uiState.username.isNullOrBlank()) {
            Text(
                "username after decryption: ${uiState.username}",
                modifier = Modifier.padding(10.dp),
            )
            Text("Password after decryption: ${uiState.password}")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RoomEnryptionTheme {
        MainScreen(
            innerPadding = PaddingValues(20.dp),
            saveUserSettings = { _, _ -> },
            {},
            UserSettingsUiState(),
        )
    }
}