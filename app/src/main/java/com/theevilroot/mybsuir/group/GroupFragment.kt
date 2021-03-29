package com.theevilroot.mybsuir.group

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.asVisibility
import com.theevilroot.mybsuir.group.data.Group
import com.theevilroot.mybsuir.group.members.GroupMembersAdapter
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_group.view.*
import org.kodein.di.generic.instance

class GroupFragment : ModelDataFragment<GroupFragment.GroupViewState, Group>(R.layout.f_group) {

     sealed class GroupViewState {
        abstract val progressVisibility: Boolean
        abstract val groupViewVisibility: Boolean
        abstract val errorViewVisibility: Boolean

        object GroupLoadingState: GroupViewState() {
            override val progressVisibility: Boolean = true
            override val groupViewVisibility: Boolean = false
            override val errorViewVisibility: Boolean = false
        }
        class GroupFilledState(val group: Group): GroupViewState() {
            override val progressVisibility: Boolean = false
            override val groupViewVisibility: Boolean = true
            override val errorViewVisibility: Boolean = false
        }
        class GroupFailedState(@DrawableRes val image: Int, val message: String, val retryHandler: View.() -> Unit): GroupViewState() {
            override val progressVisibility: Boolean = false
            override val groupViewVisibility: Boolean = false
            override val errorViewVisibility: Boolean = true
        }
    }

    private val groupModel: IGroupModel by instance()

    private val controller by lazy { GroupController(groupModel) }
    private val groupAdapter by lazy { GroupMembersAdapter() }

    override fun View.onView() {
        with(group_view) {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context)
        }

        updateData(true)
    }

    override fun getLoadingState(): GroupViewState =
            GroupViewState.GroupLoadingState

    override fun getFilledState(it: Group): GroupViewState =
            GroupViewState.GroupFilledState(it)

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): GroupViewState =
            GroupViewState.GroupFailedState(getImageForError(it), msg, retryAction)

    override fun getDataUpdate(forceUpdate: Boolean): Single<Group> =
            controller.updateGroupInfo(forceUpdate)

    override fun View.applyState(newState: GroupViewState) = with(newState) {
        group_view.visibility = groupViewVisibility.asVisibility()
        group_progress.visibility = progressVisibility.asVisibility()
        group_error.visibility = errorViewVisibility.asVisibility()
        group_error_image.visibility = errorViewVisibility.asVisibility()

        if (this is GroupViewState.GroupFailedState) {
            group_error_message.text = message
            group_retry.setOnClickListener(retryHandler)
            group_error_image.setImageResource(image)
        }

        if (this is GroupViewState.GroupFilledState) {
            groupAdapter.setGroup(group)
            group_title.text = "Группа ${group.number}"
        }
    }

}