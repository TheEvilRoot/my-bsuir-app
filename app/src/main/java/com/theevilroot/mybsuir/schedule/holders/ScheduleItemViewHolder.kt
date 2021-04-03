package com.theevilroot.mybsuir.schedule.holders

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.ScheduleEntry
import com.theevilroot.mybsuir.common.utils.asVisibility
import com.theevilroot.mybsuir.schedule.data.ScheduleItem
import kotlinx.android.synthetic.main.i_schedule_entry_item.view.*
import pl.hypeapp.materialtimelineview.MaterialTimelineView

class ScheduleItemViewHolder(itemView: View) : AbstractScheduleViewHolder(itemView) {

    override fun bind(data: ScheduleItem, isFirst: Boolean, isLast: Boolean) = with(itemView) {
        if (data !is ScheduleItem.LessonInterval)
            return@with

        timeline_entry.position = when {
            isFirst && !isLast -> MaterialTimelineView.POSITION_FIRST
            !isFirst && isLast -> MaterialTimelineView.POSITION_LAST
            else -> MaterialTimelineView.POSITION_MIDDLE
        }

        val pairedAwareVisibility = (data.isTop != false).asVisibility(false)

        schedule_entry_title.text = data.entry.subject
        schedule_entry_teacher.text = data.entry.employee
                .joinToString(", ") { it.fullName }
        schedule_entry_auditory.text = data.entry.auditory.joinToString(", ")

        schedule_entry_interval.text = data.entry.timeInterval
        schedule_entry_interval.visibility = pairedAwareVisibility

        schedule_entry_subgroup.visibility = (data.entry.subgroup != 0).asVisibility()
        schedule_entry_subgroup.text = "${data.entry.subgroup} подгруппа"

        schdule_entry_mark.backgroundTintList = ColorStateList.valueOf(data.entry.type.markColor)
        schdule_entry_mark.visibility = pairedAwareVisibility

        timeline_entry.setBackgroundResource(when (data.isTop) {
            true -> R.drawable.b_rounded_block_top
            false -> R.drawable.b_rounded_block_bottom
            null -> R.drawable.b_rounded_block
        })
    }

}