package com.theevilroot.mybsuir.common.adapters

import android.view.View

abstract class AddableViewHolder<T> (itemView: View) : SimpleViewHolder<T?>(itemView) {
    final override fun bind(data: T?, isFirst: Boolean, isLast: Boolean) {
        if (data == null) {
            bindAdd(isFirst, isLast)
        } else {
            bindItem(data, isFirst, isLast)
        }
    }

    abstract fun bindItem(data: T, isFirst: Boolean, isLast: Boolean)

    abstract fun bindAdd(isFirst: Boolean, isLast: Boolean)
}