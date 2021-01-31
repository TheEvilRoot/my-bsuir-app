package com.theevilroot.mybsuir.common.data

import androidx.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.theevilroot.mybsuir.R

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

    enum class Type (val value: String, @StringRes val hintRes: Int) {
        COMMON("обычная", R.string.paper_type_all_hint),
        SPECIAL("с гербовой печатью", R.string.paper_type_special_hint)
    }

    enum class Status(val value: Int, val description: String) {
        PRINTED(1, "Напечатана"),
        NOP(-1, "NOP") // TODO: reverse values
    }

    fun status(): Status =
        Status.values().firstOrNull { it.value == _status } ?: Status.NOP

    fun type(): Type =
        Type.values().firstOrNull { it.value == type } ?: Type.COMMON
}