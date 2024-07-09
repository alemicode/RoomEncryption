package com.example.roomenryption

import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// Source - https://medium.com/@hayk.mkrtchyan8998/shedding-light-on-android-encryption-android-crypto-api-part-2-cipher-147ff4411e1d

/**
 * For the simplicity I have used object. Consider keeping it in a class.
 * In the next lesson we'll do that way.
 */
object CryptoUtils {

    private const val AES_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$AES_ALGORITHM/$BLOCK_MODE/$PADDING"

    /**
     * The key length should be 16, 24 or 32 characters long
     * - 16 -> AES-128
     * - 24 -> AES-192
     * - 32 -> AES-256
     * @see <a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">AES Algorithm</a>
     */
    private val keyValue = "keep_this_key_sc".toByteArray(Charsets.UTF_8)

    @Throws(Exception::class)
    fun encrypt(inputText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = generateRandomIV(cipher.blockSize)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(keyValue, AES_ALGORITHM),
            IvParameterSpec(iv)
        )

        val encryptedBytes = cipher.doFinal(inputText.toByteArray())
        val encryptedDataWithIV = ByteArray(iv.size + encryptedBytes.size)
        System.arraycopy(iv, 0, encryptedDataWithIV, 0, iv.size)
        System.arraycopy(encryptedBytes, 0, encryptedDataWithIV, iv.size, encryptedBytes.size)
        return Base64.encodeToString(encryptedDataWithIV, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(data: String): String {
        val encryptedDataWithIV = Base64.decode(data, Base64.DEFAULT)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = encryptedDataWithIV.copyOfRange(0, cipher.blockSize)
        val encryptedData =
            encryptedDataWithIV.copyOfRange(cipher.blockSize, encryptedDataWithIV.size)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(keyValue, AES_ALGORITHM),
            IvParameterSpec(iv)
        )

        val decryptedBytes = cipher.doFinal(encryptedData)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun generateRandomIV(size: Int): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(size)
        random.nextBytes(iv)
        return iv
    }
}