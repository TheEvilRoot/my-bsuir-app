package com.theevilroot.mybsuir.markbook.marks

import android.view.View
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.data.Mark
import kotlinx.android.synthetic.main.i_mark.view.*

class MarkViewHolder (itemView: View): SimpleViewHolder<Mark>(itemView) {
    override fun bind(data: Mark) = with(itemView) {
        mark_title.text = data.subject
        mark_value.text = data.mark
        mark_teacher.text = data.teacher
    }

}