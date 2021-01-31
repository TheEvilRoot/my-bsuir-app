package com.theevilroot.mybsuir.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class SimpleAdapter<T, H: SimpleViewHolder<T>>(
    @LayoutRes
    internal val layout: Int,
    internal val holderFactory: (View) -> H
) : RecyclerView.Adapter<SimpleViewHolder<T>>() {

    internal var list: List<T> = listOf()

    open fun setData(items: List<T>) {
        list = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout, parent, false)
        return holderFactory(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int) {
        val item = list[position]
        holder.itemView.run { holder.run { bind(item, position == 0, isLast = position == list.lastIndex) } }
    }
}
