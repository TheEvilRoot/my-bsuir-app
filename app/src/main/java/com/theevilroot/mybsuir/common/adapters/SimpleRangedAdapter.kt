package com.theevilroot.mybsuir.common.adapters

import android.view.View
import androidx.annotation.LayoutRes
import kotlin.math.abs

class SimpleRangedAdapter<T, VH: SimpleViewHolder<T>>(
    @LayoutRes layoutRes: Int,
    holderFactory: (View) -> VH,
) : SimpleAdapter<T, VH>(layoutRes, holderFactory) {

    var onItemClick: ((T) -> Unit)? = null
        set(value) { field = value; notifyDataSetChanged() }

    override fun setData(items: List<T>) {
        val oldSize = list.size
        val newSize = items.size

        list = items

        if (oldSize == newSize && oldSize == 0)
            return

        if (oldSize == newSize) {
            return notifyItemRangeChanged(0, newSize)
        }
        if (newSize == 0) {
            return notifyItemRangeRemoved(0, oldSize)
        }
        if (oldSize == 0) {
            return notifyItemRangeInserted(0, newSize)
        }

        val delta = abs(newSize - oldSize)
        if (oldSize > newSize) { // remove some elements
            notifyItemRangeChanged(0, newSize)
            notifyItemRangeRemoved(newSize - 1, delta)
        } else { // add some elements
            notifyItemRangeChanged(0, oldSize)
            notifyItemRangeInserted(oldSize - 1, delta)
        }
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        val listener = onItemClick
        if (listener == null)
            holder.itemView.setOnClickListener(null)
        else holder.itemView.setOnClickListener { listener(list[position]) }
    }

}