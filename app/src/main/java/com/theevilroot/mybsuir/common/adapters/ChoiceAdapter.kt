package com.theevilroot.mybsuir.common.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class ChoiceAdapter<T, BVH: ChoiceViewHolder<T>, VH: BVH, SVH: BVH>(
    @LayoutRes
    private val layoutRes: Int,
    @LayoutRes
    private val selectedLayoutRes: Int,
    private val holderFactory: (View) -> VH,
    private val selectedHolderFactory: (View) -> SVH,
    private val onSelectChanged: (Int, T?) -> Unit,
) : RecyclerView.Adapter<ChoiceViewHolder<T>>() {

    private val viewTypeSelected = 998
    private val viewTypeNormal = 0

    private var selectedPosition = -1
    private var data = listOf<T>()

    var allowUnSelection = true
        set(value) {
            field = value
            if (!value && selectedPosition < 0)
                selectedPosition = 0
            notifyDataSetChanged()
        }
    var allowInteraction: Boolean = true

    private fun notifySelectionChanged(index: Int) {
        if (index in 0 until itemCount)
            notifyItemChanged(index)
    }

    private fun toggleSelection(index: Int) {
        if (!allowInteraction)
            return
        if (allowUnSelection && selectedPosition == index)
            clearSelection()
        else select(index)
    }

    private fun clearSelection() {
        val oldSelection = selectedPosition
        selectedPosition = -1
        notifySelectionChanged(oldSelection)

        onSelectChanged(-1, null)
    }

    fun select(index: Int) {
        if (index < 0)
            return
        if (index == selectedPosition)
            return

        val oldSelection = selectedPosition
        selectedPosition = index
        notifySelectionChanged(selectedPosition)
        notifySelectionChanged(oldSelection)

        onSelectChanged(index, data[index])
    }

    fun setData(items: List<T>) {
        selectedPosition = if (allowUnSelection) -1 else 0
        data = items
        notifyDataSetChanged()
        onSelectChanged(-1, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val view = if (viewType == viewTypeSelected) {
            inflater.inflate(selectedLayoutRes, parent, false)
        } else {
            inflater.inflate(layoutRes, parent, false)
        }
        val holder = if (viewType == viewTypeSelected) {
            selectedHolderFactory
        } else {
            holderFactory
        }
        return holder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == selectedPosition) viewTypeSelected else viewTypeNormal
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: ChoiceViewHolder<T>, position: Int) {
        holder.bind(data[position], position == 0,
            position == data.lastIndex, position == selectedPosition)
        holder.itemView.setOnClickListener {
            toggleSelection(position)
        }
    }
}