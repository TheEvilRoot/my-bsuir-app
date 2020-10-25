package com.theevilroot.mybsuir.profile.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.Skill

class SkillsAdapter : RecyclerView.Adapter<SkillsViewHolder>() {

    private var list: List<Skill> = listOf()

    fun setSkills(skills: List<Skill>) {
        list = skills
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.i_skill, parent, false)
        return SkillsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        val skill = list[position]
        holder.setValue(skill.name)
    }
}
