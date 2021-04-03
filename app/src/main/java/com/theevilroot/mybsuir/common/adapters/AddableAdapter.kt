package com.theevilroot.mybsuir.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class AddableAdapter<T, VH: AddableViewHolder<T>> (
    @LayoutRes val itemLayout: Int,
    @LayoutRes val addLayout: Int,
    val itemHolderFactory: (View) -> VH,
    val addHolderFactory: (View) -> VH,
    val onAddListener: View.OnClickListener,
) : RecyclerView.Adapter<VH>() {

    protected var list: List<T> = emptyList()

    private val viewTypeItem = 0
    private val viewTypeAdd = 1

    fun setData(data: List<T>) {
        list = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position in list.indices)
            return viewTypeItem
        return viewTypeAdd
    }

    @LayoutRes
    private fun layoutByViewType(viewType: Int): Int {
        return when (viewType) {
            viewTypeAdd -> addLayout
            else -> itemLayout
        }
    }

    private fun viewHolderByViewType(view: View, viewType: Int): VH {
        return when (viewType) {
            viewTypeAdd -> addHolderFactory(view)
            else -> itemHolderFactory(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = layoutByViewType(viewType)
        val view = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return viewHolderByViewType(view, viewType)
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position in list.indices) {
            val item = list[position]
            holder.bind(item, isFirst = position == 0, isLast = position == list.lastIndex)
            holder.itemView.setOnClickListener { }
        } else {
            holder.bind(null, isFirst = position == 0, isLast = true)
            holder.itemView.setOnClickListener(onAddListener)
        }
    }
}