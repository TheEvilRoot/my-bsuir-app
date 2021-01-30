package com.theevilroot.mybsuir.common.adapters

import android.graphics.Color
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.theevilroot.mybsuir.R

data class ErrorDescriptor (
    val title: String? = "Ошибка",
    val message: String? = null,
    @DrawableRes
    val imageRes: Int = R.drawable.ic_round_error,
    @StringRes
    val messageRes: Int = 0,
    @StringRes
    val titleRes: Int = 0,
    val backgroundColor: Int = Color.rgb(0xf3, 0xf3, 0xf3),
    val retryAction: View.() -> Unit = { }
)