package com.theevilroot.mybsuir.profile.holders

import android.view.View
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.data.Skill
import kotlinx.android.synthetic.main.i_skill.view.*

class SkillsViewHolder(itemView: View): SimpleViewHolder<Skill>(itemView) {
    override fun bind(data: Skill) = with(itemView) {
        value.text = data.name
    }

}