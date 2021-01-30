package com.theevilroot.mybsuir.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.theevilroot.mybsuir.R

class ErrorAwareAdapter<T, H: SimpleViewHolder<T>>(
    @LayoutRes layoutRes: Int,
    holderFactory: (View) -> H,
) : SimpleAdapter<T, H>(layoutRes, holderFactory) {

    var error: ErrorDescriptor? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val errorViewType = 999

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<T> {
        if (viewType == errorViewType) {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.i_error_item, parent, false)
            return ErrorViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (error != null) errorViewType else super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (error != null) 1 else super.getItemCount()
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int) {
        val error = error
        if (error != null && position == 0) {
            (holder as? ErrorViewHolder)?.bindError(error)
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

}