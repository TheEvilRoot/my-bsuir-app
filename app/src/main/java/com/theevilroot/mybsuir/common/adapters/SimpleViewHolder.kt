package com.theevilroot.mybsuir.common.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class SimpleViewHolder<T>(
    private val binding: View.(T) -> Unit,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: T) = with(itemView) {
        binding(data)
    }

}