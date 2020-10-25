package com.theevilroot.mybsuir.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.data.Skill
import kotlin.math.max

open class SimpleAdapter<T> (
    @LayoutRes
    private val layout: Int,
    private val binding: View.(T) -> Unit
) : RecyclerView.Adapter<SimpleViewHolder<T>>() {

    private var list: List<T> = listOf()

    fun setData(items: List<T>) {
        list = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout, parent, false)
        return SimpleViewHolder(binding, view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<T>, position: Int) {
        val item = list[position]
        holder.itemView.run { holder.bind(item) }
    }
}
