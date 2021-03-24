package com.theevilroot.mybsuir.markbook.marks

import android.view.View
import android.widget.TextView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.bind
import com.theevilroot.mybsuir.common.data.Mark
import com.theevilroot.mybsuir.common.asVisibility

sealed class AbstractMarkViewHolder(itemView: View): SimpleViewHolder<Mark>(itemView)

class PassedMarkViewHolder(itemView: View): AbstractMarkViewHolder(itemView) {

    private val markTitle by bind<TextView>(R.id.mark_title)
    private val markValue by bind<TextView>(R.id.mark_value)
    private val markTeacher by bind<TextView>(R.id.mark_teacher)

    override fun bind(data: Mark, isFirst: Boolean, isLast: Boolean) = with(itemView) {
        markTitle.text = data.subject
        markValue.text = data.mark
        markTeacher.text = data.teacher

        markTeacher.visibility = data.teacher?.isNotEmpty().asVisibility()
    }
}

class PendingMarkViewHolder(itemView: View): AbstractMarkViewHolder(itemView) {

    private val markTitle by bind<TextView>(R.id.mark_title)
    private val markAverageValue by bind<TextView>(R.id.mark_average_value)
    private val markValueLabel by bind<TextView>(R.id.mark_average_value_label)
    private val markType by bind<TextView>(R.id.mark_type)
    private val markRetakes by bind<TextView>(R.id.mark_average_retakes)
    private val markRetakesLabel by bind<TextView>(R.id.mark_average_retakes_label)

    override fun bind(data: Mark, isFirst: Boolean, isLast: Boolean) {
        markTitle.text = data.subject
        markAverageValue.text = String.format("%.2f", data.commonMark)
        markType.text = data.form
        markRetakes.text = "${(data.commonRetakes * 100).toInt()}%"

        (data.commonMark > 0.0).asVisibility().let {
            markAverageValue.visibility = it
            markValueLabel.visibility = it
        }
        (data.commonRetakes > 0.0).asVisibility().let {
            markRetakes.visibility = it
            markRetakesLabel.visibility = it
        }
    }

}