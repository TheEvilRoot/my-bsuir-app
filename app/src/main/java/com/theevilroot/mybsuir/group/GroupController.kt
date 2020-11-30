package com.theevilroot.mybsuir.group

import com.theevilroot.mybsuir.common.data.GroupMember
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.SpecialGroupMember
import com.theevilroot.mybsuir.group.data.Group
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class GroupController (
        val model: GroupModel,
) {

    fun updateGroupInfo(forceUpdate: Boolean): Single<Group> =
            Single.create<Group> {
                val groupInfo = model.getGroupInfo(allowCache = !forceUpdate)
                        ?: return@create it.onError(InternalException("Невозможно получить данные о группе"))
                val supervisorFilter = { m: GroupMember -> m.position == "Куратор" }
                val supervisor = groupInfo.members.firstOrNull(supervisorFilter)
                val leader = groupInfo.members.firstOrNull { m -> m.position == "Староста группы" }
                val group = Group(
                        groupInfo.numberGroup,
                        supervisor?.let(::SpecialGroupMember),
                        leader?.let(::SpecialGroupMember),
                        groupInfo.members
                                .filterNot(supervisorFilter)
                                .sortedBy(GroupMember::fullName)
                )
                it.onSuccess(group)
            }.subscribeOn(Schedulers.io())

}