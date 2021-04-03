package com.theevilroot.mybsuir.profile.adapters

import android.view.View
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.AddableAdapter
import com.theevilroot.mybsuir.common.data.Skill
import com.theevilroot.mybsuir.profile.holders.SkillsViewHolder

class SkillsAdapter(
    onAddListener: View.OnClickListener,
    private val onItemClickListener: (Skill, Int) -> Unit,
    private val onRemoveClick: (Skill) -> Unit
): AddableAdapter<Skill, SkillsViewHolder>(
    R.layout.i_skill,
    R.layout.i_skill_add,
    ::SkillsViewHolder,
    ::SkillsViewHolder,
    onAddListener,
) {

    var removableIndex: Int = -1
        set(value) {
            val old = field
            field = value
            if (value in list.indices) notifyItemChanged(value)
            if (old in list.indices) notifyItemChanged(old)
        }

    fun cleanRemovable() {
        removableIndex = -1
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if (position in list.indices)
            holder.setOnClickListener { onItemClickListener(list[position], position) }

        val isRemovable = position == removableIndex
        if (isRemovable)
            holder.setRemovable(isRemovable) { onRemoveClick(list[position]) }
        else if (position in list.indices)
            holder.setRemovable(isRemovable, null)
    }

}