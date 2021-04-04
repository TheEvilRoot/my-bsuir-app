package com.theevilroot.mybsuir.common.encryption

import com.theevilroot.mybsuir.common.encryption.base.IEncryptionLayer

object NullEncryptionLayer : IEncryptionLayer {
    override fun encrypt(data: ByteArray): ByteArray {
        return data
    }

    override fun decrypt(encryptedData: ByteArray): ByteArray {
        return encryptedData
    }

}