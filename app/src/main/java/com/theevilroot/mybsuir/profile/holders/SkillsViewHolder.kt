package com.theevilroot.mybsuir.profile.holders

import android.view.View
import com.theevilroot.mybsuir.common.adapters.AddableViewHolder
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.data.Skill
import kotlinx.android.synthetic.main.i_skill.view.*

class SkillsViewHolder(itemView: View): AddableViewHolder<Skill>(itemView) {

    override fun bindItem(data: Skill, isFirst: Boolean, isLast: Boolean) = with(itemView) {
        value.text = data.name
    }

    override fun bindAdd(isFirst: Boolean, isLast: Boolean) = with(itemView) { }

}