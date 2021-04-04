package com.theevilroot.mybsuir.common.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import com.theevilroot.mybsuir.common.encryption.base.AndroidEncryptionLayer
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
class KeyStoreEncryptionLayer : AndroidEncryptionLayer() {

    private val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

    override fun encrypt(data: ByteArray): ByteArray {
        val spec  = KeyGenParameterSpec.Builder("keystore",
                KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        keyGenerator.init(spec)
        val secretKey = keyGenerator.generateKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val initVector = cipher.iv
        val encryptedData = cipher.doFinal(data)

        Log.d("KeyStore", "encrypt/iv length: ${initVector.size} data length: ${encryptedData.size}")
        return packData(initVector, encryptedData)
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKey = (keyStore.getEntry("keystore", null)
                as KeyStore.SecretKeyEntry).secretKey

        val (iv, payload) = unpackData(encryptedData)
                ?: throw IllegalArgumentException("Failed to unpack encrypted data")

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv, 0, iv.size)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        return cipher.doFinal(payload, 0, payload.size)
    }
}