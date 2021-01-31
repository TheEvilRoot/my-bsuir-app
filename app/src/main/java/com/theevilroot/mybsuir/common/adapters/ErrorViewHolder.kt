package com.theevilroot.mybsuir.common.adapters

import android.view.View
import kotlinx.android.synthetic.main.i_error_item.view.*
import java.lang.UnsupportedOperationException

class ErrorViewHolder<T>(itemView: View) : SimpleViewHolder<T>(itemView) {

    override fun bind(data: T, isFirst: Boolean, isLast: Boolean) {
        throw UnsupportedOperationException()
    }

    fun bindError(descriptor: ErrorDescriptor) = with(itemView) {
        descriptor.title?.let { error_title.text = it }
                ?: error_title.setText(descriptor.titleRes)
        descriptor.message?.let { error_message.text = it }
                ?: error_title.setText(descriptor.messageRes)
        error_image.setImageResource(descriptor.imageRes)
        error_retry.setOnClickListener { descriptor.retryAction(this) }
        error_card.setCardBackgroundColor(descriptor.backgroundColor)
    }
}