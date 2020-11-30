package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class Paper(
    val id: Int,
    val provisionPlace: String,
    val dateOrder: String,
    @SerializedName("certificateType")
    val type: String,
    @SerializedName("status")
    private val _status: Int,
    val rejectionReason: String?
) {
    enum class Status(val value: Int, val description: String) {
        PRINTED(1, "Напечатана"),
        NOP(-1, "NOP") // TODO: reverse values
    }

    fun status(): Status =
        Status.values().firstOrNull { it.value == _status } ?: Status.NOP
}