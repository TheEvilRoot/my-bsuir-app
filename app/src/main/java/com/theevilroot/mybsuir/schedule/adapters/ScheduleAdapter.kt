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

class ScheduleAdapter (
    private val dayStartTitle: String,
    private val dayEndTitle: String
) : RecyclerView.Adapter<AbstractScheduleViewHolder>() {

    private val viewTypeLesson = 1
    private val viewTypeRest = 2

    private var data: List<ScheduleItem> = emptyList()

    fun setSchedule(list: List<ScheduleEntry>) {
        data = mutableListOf<ScheduleItem>().apply {
            if (list.isEmpty())
                return@apply

            add(ScheduleItem.RestInterval(null, dayStartTitle, list.first().startTime))
            var previous: ScheduleEntry? = null
            for ((index, entry) in list.withIndex()) {
                val isNotPaired = previous?.startTime != entry.startTime
                if (previous != null && isNotPaired) {
                    add(ScheduleItem.RestInterval(previous.endTime, "${
                        previous.endTime.between(entry.startTime).totalMinutes
                    } минут перерыв", entry.startTime))
                }
                val pairedWithNext = index != list.lastIndex
                        && list[index + 1].startTime == entry.startTime
                add(ScheduleItem.LessonInterval(entry,
                    when {
                        pairedWithNext -> true
                        !pairedWithNext && !isNotPaired -> false
                        else -> null
                    }
                ))
                previous = entry
            }
            add(ScheduleItem.RestInterval(previous?.endTime, dayEndTitle, null))
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