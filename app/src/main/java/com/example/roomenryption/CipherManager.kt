package com.example.roomenryption

interface CipherManager {
    /**
     * Generic function to encrypt our data
     * @param inputText - the data we want to encrypt
     * @return the encrypted data
     */
    @Throws(Exception::class)
    fun encrypt(inputText: String): String // Consider returning ByteArray instead of String

    /**
     * Generic function to decrypt our data. Consider passing a ByteArray.
     * @param data - the data we want to decrypt.
     * @return the decrypted data
     */
    @Throws(Exception::class)
    fun decrypt(data: String): String
}