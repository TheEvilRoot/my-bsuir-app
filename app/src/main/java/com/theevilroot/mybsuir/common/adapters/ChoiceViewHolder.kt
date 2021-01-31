package com.theevilroot.mybsuir.common.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ChoiceViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: T, isFirst: Boolean, isLast: Boolean, isSelected: Boolean)

}