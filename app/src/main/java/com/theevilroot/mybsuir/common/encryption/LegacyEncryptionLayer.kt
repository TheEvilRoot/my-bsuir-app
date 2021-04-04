package com.theevilroot.mybsuir.common.encryption

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Log
import com.theevilroot.mybsuir.common.encryption.base.AndroidEncryptionLayer
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal
import kotlin.random.Random

class LegacyEncryptionLayer(private val context: Context) : AndroidEncryptionLayer() {

    private val keyGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

    override fun encrypt(data: ByteArray): ByteArray {
        val spec  = KeyPairGeneratorSpec.Builder(context)
            .setAlias("keystore")
            .setSubject(X500Principal("CN=keystore"))
            .setSerialNumber("keystore".hashCode().toBigInteger())
            .setStartDate(Calendar.getInstance().time)
            .setEndDate(Calendar.getInstance().apply { add(Calendar.YEAR, 30) }.time)
            .build()

        keyGenerator.initialize(spec)
        val keyPair = keyGenerator.generateKeyPair()

        val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.public)

        val encryptedData = cipher.doFinal(data)

        Log.d("KeyStore", "encrypt/data length: ${encryptedData.size}")
        return packData(Random.nextBytes(16), encryptedData)
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val privateKey = (keyStore.getEntry("keystore", null)
                as KeyStore.PrivateKeyEntry).privateKey

        val (_, payload) = unpackData(encryptedData)
            ?: throw IllegalArgumentException("Failed to unpack encrypted data")

        val cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        return cipher.doFinal(payload)
    }
}