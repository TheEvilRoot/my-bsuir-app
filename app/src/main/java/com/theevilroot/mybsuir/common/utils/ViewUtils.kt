package com.theevilroot.mybsuir.common.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

fun Boolean.asVisibility(gone: Boolean = true) = if (this) View.VISIBLE else (if (gone) View.GONE else View.INVISIBLE)

fun Boolean?.asVisibility() = if (this != null && this) View.VISIBLE else View.GONE

fun <T: View> RecyclerView.ViewHolder.bind(@IdRes res: Int) = lazy {
    itemView.findViewById<T>(res)
}