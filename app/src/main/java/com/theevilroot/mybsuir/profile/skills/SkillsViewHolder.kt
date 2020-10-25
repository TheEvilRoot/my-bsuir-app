package com.theevilroot.mybsuir.profile.skills

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.i_skill.view.*

class SkillsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setValue(v: String) = with(itemView) {
        value.text = v
    }

}