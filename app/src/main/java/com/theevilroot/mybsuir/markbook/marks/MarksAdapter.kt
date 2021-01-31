package com.theevilroot.mybsuir.markbook.marks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.Mark

class MarksAdapter : RecyclerView.Adapter<AbstractMarkViewHolder>() {

    private val viewTypePassed: Int = 0
    private val viewTypePending: Int = 1

    private var data: List<Mark> = emptyList()

    fun setData(items: List<Mark>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractMarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when (viewType) {
            viewTypePending -> R.layout.i_pending_mark_view
            else -> R.layout.i_mark
        }
        val view = inflater.inflate(layout, parent, false)
        return when (viewType) {
            viewTypePending -> PendingMarkViewHolder(view)
            else -> PassedMarkViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].mark.isEmpty()) viewTypePending else viewTypePassed
    }

    override fun onBindViewHolder(holder: AbstractMarkViewHolder, position: Int) {
        holder.bind(data[position], position == 0, position == data.lastIndex)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

}