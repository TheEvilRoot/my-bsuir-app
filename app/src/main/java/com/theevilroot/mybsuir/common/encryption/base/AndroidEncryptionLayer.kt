package com.theevilroot.mybsuir.common.encryption.base

import android.util.Log
import com.theevilroot.mybsuir.common.utils.JavaUtils

abstract class AndroidEncryptionLayer : IEncryptionLayer {

    private fun <T> Iterable<T>.fullZip(other: Iterable<T>): Sequence<Pair<T?, T?>> {
        val first = iterator()
        val second = other.iterator()
        return generateSequence {
            if (first.hasNext() && second.hasNext())
                first.next() to second.next()
            else if (first.hasNext() && !second.hasNext())
                first.next() to null
            else if (!first.hasNext() && second.hasNext())
                null to second.next()
            else null
        }
    }

    fun packData(iv: ByteArray, data: ByteArray): ByteArray {
        val ivLength = iv.size
        val dataLength = data.size
        return JavaUtils.serializeInt(ivLength) +
                JavaUtils.serializeInt(dataLength) +
                iv.asIterable().fullZip(data.asIterable())
                    .flatMap { (a, b) -> listOfNotNull(a, b) }
                    .toList().toByteArray()
    }

    fun unpackData(data: ByteArray): Pair<ByteArray, ByteArray>? {
        if (data.size < 8)
            return null

        val ivLength = JavaUtils.deserializeInt(data, 0)
        if (ivLength < 0 || data.size < ivLength)
            return null

        val dataLength = JavaUtils.deserializeInt(data, 4)
        if (dataLength < 0 || data.size < dataLength + ivLength)
            return null

        Log.d("KeyStore", "unpackData/dataLength: $dataLength ivLength: $ivLength inputLength: ${data.size}")
        val dataBuffer = mutableListOf<Byte>()
        val ivBuffer = mutableListOf<Byte>()

        var index = 8
        while (index < data.size) {
            if (ivBuffer.size < ivLength)
                ivBuffer.add(data[index++])
            if (index < data.size && dataBuffer.size < dataLength)
                dataBuffer.add(data[index++])
            if (dataBuffer.size == dataLength && ivBuffer.size == ivLength)
                break
        }

        if (dataBuffer.size != dataLength || ivBuffer.size != ivLength) {
            Log.e("KeyStore", "unpackData/check fail data length: $dataLength " +
                    "data buffer: ${dataBuffer.size} iv length: $ivLength iv buffer: ${ivBuffer.size}")
            return null
        }

        return ivBuffer.toByteArray() to dataBuffer.toByteArray()
    }

}