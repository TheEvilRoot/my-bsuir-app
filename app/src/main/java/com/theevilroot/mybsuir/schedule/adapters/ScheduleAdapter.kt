package com.theevilroot.mybsuir.schedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.ScheduleEntry
import com.theevilroot.mybsuir.schedule.data.ScheduleItem
import com.theevilroot.mybsuir.schedule.holders.AbstractScheduleViewHolder
import com.theevilroot.mybsuir.schedule.holders.ScheduleItemViewHolder
import com.theevilroot.mybsuir.schedule.holders.ScheduleLineViewHolder

class ScheduleAdapter : RecyclerView.Adapter<AbstractScheduleViewHolder>() {

    private val viewTypeLesson = 1
    private val viewTypeRest = 2

    private var data: List<ScheduleItem> = emptyList()

    fun setSchedule(list: List<ScheduleEntry>) {
        data = mutableListOf<ScheduleItem>().apply {
            if (list.isEmpty())
                return@apply

            add(ScheduleItem.RestInterval(null, "Start of the day", list.first().startTime))
            var previous: ScheduleEntry? = null
            for (entry in list) {
                if (previous != null) {
                    add(ScheduleItem.RestInterval(previous.endTime, "Rest", entry.startTime))
                }
                add(ScheduleItem.LessonInterval(entry))
                previous = entry
            }
            add(ScheduleItem.RestInterval(previous?.endTime, "End of the day", null))
        }
        notifyDataSetChanged()
    }

    @LayoutRes
    private fun Int.layoutRes(): Int = when (this) {
        viewTypeLesson -> R.layout.i_schedule_entry_item
        else -> R.layout.i_schedule_entry_line
    }

    private fun Int.holder(view: View): AbstractScheduleViewHolder = when(this) {
        viewTypeLesson -> ScheduleItemViewHolder(view)
        else -> ScheduleLineViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is ScheduleItem.LessonInterval -> viewTypeLesson
            is ScheduleItem.RestInterval -> viewTypeRest
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType.layoutRes(), parent, false)
        return viewType.holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AbstractScheduleViewHolder, position: Int) {
//        val isFirst = (position == 0 && data.size == 1) || (position == 1 && data.size > 1)
//        val isLast = (position == data.lastIndex && data.size == 1) || (position == data.lastIndex - 1 && data.size > 1)

        holder.bind(data[position], position == 0, position == data.lastIndex)
    }

}