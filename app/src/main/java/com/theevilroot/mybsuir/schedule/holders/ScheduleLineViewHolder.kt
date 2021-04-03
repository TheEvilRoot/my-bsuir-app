package com.theevilroot.mybsuir.schedule.holders

import android.view.View
import com.theevilroot.mybsuir.common.utils.asVisibility
import com.theevilroot.mybsuir.schedule.data.ScheduleItem
import kotlinx.android.synthetic.main.i_schedule_entry_line.view.*
import pl.hypeapp.materialtimelineview.MaterialTimelineView

class ScheduleLineViewHolder(itemView: View) : AbstractScheduleViewHolder(itemView) {

    override fun bind(data: ScheduleItem, isFirst: Boolean, isLast: Boolean) = with(itemView) {
        if (data !is ScheduleItem.RestInterval)
            return@with

        timeline_entry.position = when {
            isFirst && !isLast -> MaterialTimelineView.POSITION_FIRST
            !isFirst && isLast -> MaterialTimelineView.POSITION_LAST
            else -> MaterialTimelineView.POSITION_MIDDLE
        }

        timeline_entry.timelineType = MaterialTimelineView.TIMELINE_TYPE_LINE
        timeline_entry.isEnabled = false

        schedule_line_title.text = data.title
        schedule_line_top.text = "${data.start ?: ""}"
        schedule_line_bottom.text = "${data.end ?: ""}"

        schedule_line_top.visibility = (data.start != null).asVisibility()
        schedule_line_bottom.visibility = (data.end != null).asVisibility()
    }

}