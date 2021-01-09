package com.theevilroot.mybsuir.papers.holders

import android.view.View
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.asVisibility
import kotlinx.android.synthetic.main.i_paper.view.*

class PaperViewHolder(itemView: View) : SimpleViewHolder<Paper>(itemView) {
    override fun bind(data: Paper) = with(itemView) {
        title.text = data.provisionPlace
        date.text = data.dateOrder
        seal.visibility = (data.status() == Paper.Status.PRINTED).asVisibility()
        status.text = data.status().description
    }
}