package com.example.roomenryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class CipherManagerImpl : CipherManager {

    private val keyAlias = "aes_key_alias" // TODO - Better to keep it secure

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null) // With load function we initialize our keystore
    }

    /**
     * Gets a key from the keystore. If it doesn't exist, it creates a new one
     */
    @Throws(Exception::class)
    private fun getOrCreateKey(): SecretKey {
        val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    /**
     * Creates a new key using KeyGenerator and returns it
     * First we initialize our KeyGenerator by passing KeyGenParameterSpec and then we generate the key
     */
    @Throws(Exception::class)
    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(AES_ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    @Throws(Exception::class)
    override fun encrypt(inputText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())

        val encryptedBytes = cipher.doFinal(inputText.toByteArray())
        val iv = cipher.iv

        val encryptedDataWithIV = ByteArray(iv.size + encryptedBytes.size)
        System.arraycopy(iv, 0, encryptedDataWithIV, 0, iv.size)
        System.arraycopy(encryptedBytes, 0, encryptedDataWithIV, iv.size, encryptedBytes.size)
        return Base64.encodeToString(encryptedDataWithIV, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    override fun decrypt(data: String): String {
        val encryptedDataWithIV = Base64.decode(data, Base64.DEFAULT)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = encryptedDataWithIV.copyOfRange(0, cipher.blockSize)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), IvParameterSpec(iv))

        val encryptedData =
            encryptedDataWithIV.copyOfRange(cipher.blockSize, encryptedDataWithIV.size)
        val decryptedBytes = cipher.doFinal(encryptedData)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val AES_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$AES_ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}