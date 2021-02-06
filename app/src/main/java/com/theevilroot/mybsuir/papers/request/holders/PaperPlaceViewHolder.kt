package com.theevilroot.mybsuir.papers.request.holders

import android.view.View
import android.widget.TextView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.ChoiceViewHolder
import com.theevilroot.mybsuir.common.data.PaperPlace

class PaperPlaceViewHolder(itemView: View) : ChoiceViewHolder<PaperPlace>(itemView) {

    override fun bind(data: PaperPlace, isFirst: Boolean, isLast: Boolean, isSelected: Boolean) = with(itemView) {
        findViewById<TextView>(R.id.choose_item_label).let {
            it.isSelected = isSelected
            it.text = data.name
        }
        findViewById<View>(R.id.choose_item_container).let {
            it.isSelected = isSelected
            it.setBackgroundResource(R.drawable.b_left_right_edge)
        }
    }


}