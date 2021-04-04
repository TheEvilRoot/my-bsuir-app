package com.theevilroot.mybsuir.preferences.adapters

import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.preferences.data.PreferenceEntry
import com.theevilroot.mybsuir.preferences.data.PreferenceType
import com.theevilroot.mybsuir.preferences.holders.CheckablePreferenceViewHolder


class PreferencesAdapter(
    private val onPreferenceClick: (PreferenceEntry) -> Unit
) : SimpleAdapter<PreferenceEntry, CheckablePreferenceViewHolder>(
    R.layout.i_checkable_preference, ::CheckablePreferenceViewHolder
) {

    fun toggleByType(type: PreferenceType) {
        val index = list.indexOfFirst { it.type == type }
        if (index < 0)
            return
        list = list.map { if (it.type == type) it.toggled() else it }
        notifyItemChanged(index)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<PreferenceEntry>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            onPreferenceClick(list[position])
        }
    }

}