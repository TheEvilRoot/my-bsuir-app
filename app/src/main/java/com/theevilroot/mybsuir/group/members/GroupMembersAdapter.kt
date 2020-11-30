package com.theevilroot.mybsuir.group.members

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.GroupMember
import com.theevilroot.mybsuir.common.data.SpecialGroupMember
import com.theevilroot.mybsuir.group.data.Group

class GroupMembersAdapter: RecyclerView.Adapter<AbstractGroupMemberViewHolder>() {

    private val viewTypeMember = 0
    private val viewTypeSpecial = 1

    private var members: List<GroupMember> = listOf()

    fun setGroup(group: Group) {
        mutableListOf<GroupMember>().apply {
            group.supervisor?.let { add(it) }
            group.leader?.let { add(it) }
            addAll(group.students)
        }.also {
            members = it
        }
        notifyDataSetChanged()
    }

    private val viewTypeLayout = mapOf(
            viewTypeMember to R.layout.i_group_member,
            viewTypeSpecial to R.layout.i_group_special_member
    )
    private val viewTypeHolder = mapOf(
            viewTypeMember to { v: View -> GroupMemberViewHolder(v) },
            viewTypeSpecial to { v: View -> SpecialGroupMemberViewHolder(v) }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractGroupMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewTypeLayout.getValue(viewType), parent, false)
        return viewTypeHolder.getValue(viewType)(view)
    }


    override fun getItemViewType(position: Int): Int {
        return if (members[position] is SpecialGroupMember) viewTypeSpecial else viewTypeMember
    }
    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: AbstractGroupMemberViewHolder, position: Int) {
        val item = members[position]
        holder.bind(item)
    }

}