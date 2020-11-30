package com.theevilroot.mybsuir.common

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE

fun Boolean?.visibility() = if (this != null && this) View.VISIBLE else View.GONE

fun <T: View> RecyclerView.ViewHolder.bind(@IdRes res: Int) = lazy {
    itemView.findViewById<T>(res)
}