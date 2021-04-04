package com.theevilroot.mybsuir

import com.theevilroot.mybsuir.common.encryption.KeyStoreEncryptionLayer
import com.theevilroot.mybsuir.common.utils.JavaUtils
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class TestKeyStore {

    @Test
    fun testPackUnpack() {
        val dataSize = Random.nextInt(64, 512)
        val data = Random.nextBytes(dataSize)
        val ivSize = Random.nextInt(12, 24)
        val iv = Random.nextBytes(ivSize)

        val layer = KeyStoreEncryptionLayer()
        val packed = layer.packData(iv, data)
        val (unpackedIv, unpackedData) = layer.unpackData(packed)
                ?: return Assert.fail("Unpack returned null")

        Assert.assertArrayEquals(data, unpackedData)
        Assert.assertArrayEquals(iv, unpackedIv)
    }

}