package com.theevilroot.mybsuir.papers.request.holders

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.ChoiceViewHolder
import com.theevilroot.mybsuir.common.data.Paper

class PaperTypeViewHolder(itemView: View) : ChoiceViewHolder<Paper.Type>(itemView) {

    override fun bind(data: Paper.Type, isFirst: Boolean, isLast: Boolean, isSelected: Boolean) = with(itemView) {
        findViewById<TextView>(R.id.type_name).let {
            it.isSelected = isSelected
            it.text = data.value
        }
        findViewById<View>(R.id.type_container).let {
            it.isSelected = isSelected
            it.setBackgroundResource(when {
                isFirst && !isLast -> R.drawable.b_left_edge
                isLast && !isFirst -> R.drawable.b_right_edge
                isLast && isFirst -> R.drawable.b_left_right_edge
                else -> R.drawable.b_none_edge
            })
        }
    }

}