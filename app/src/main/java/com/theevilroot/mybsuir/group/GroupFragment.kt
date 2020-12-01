package com.theevilroot.mybsuir.group

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import com.theevilroot.mybsuir.common.visibility
import com.theevilroot.mybsuir.group.data.Group
import com.theevilroot.mybsuir.group.members.GroupMembersAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.f_group.view.*
import org.kodein.di.generic.instance
import java.net.UnknownHostException

class GroupFragment : BaseFragment(R.layout.f_group) {

    private  sealed class GroupViewState {
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
        class GroupFailedState(val message: String, val retryHandler: View.() -> Unit): GroupViewState() {
            override val progressVisibility: Boolean = false
            override val groupViewVisibility: Boolean = false
            override val errorViewVisibility: Boolean = true
        }
    }

    private val groupModel: GroupModel by instance()
    private val cacheController: CacheController by instance()

    private val controller by lazy { GroupController(groupModel) }
    private val groupAdapter by lazy { GroupMembersAdapter() }

    override fun View.onView() {
        with(group_view) {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context)
        }

        updateGroup(true)
    }

    private fun View.updateGroup(useCurrentCredentials: Boolean) {
        applyState(GroupViewState.GroupLoadingState)
        cacheController.preloadCacheAndCall(controller
                .updateGroupInfo(false)
                .observeOn(AndroidSchedulers.mainThread()), useCurrentCredentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ groupUpdateHandler(it) }) {
                    groupUpdateErrorHandler(it) }
    }

    private fun View.groupUpdateHandler(it: Group) =
            applyState(GroupViewState.GroupFilledState(it))

    private fun View.groupUpdateErrorHandler(it: Throwable) {
        it.printStackTrace()
        applyState(
                GroupViewState.GroupFailedState(when (it) {
                    is InternalException ->
                        it.msg
                    is NoCredentialsException ->
                        return findNavController().navigate(R.id.fragment_login)
                    is ReAuthRequiredException ->
                        return updateGroup(false)
                    is UnknownHostException ->
                        context.getString(R.string.no_internet_error)
                    else -> context.getString(R.string.unexpected_error,
                            it.javaClass.simpleName, it.localizedMessage)
                }) { updateGroup(true) })
    }

    private fun View.applyState(newState: GroupViewState) = with(newState) {
        group_view.visibility = groupViewVisibility.visibility()
        group_progress.visibility = progressVisibility.visibility()
        group_error.visibility = errorViewVisibility.visibility()

        if (this is GroupViewState.GroupFailedState) {
            group_error_message.text = message
            group_retry.setOnClickListener(retryHandler)
        }

        if (this is GroupViewState.GroupFilledState) {
            groupAdapter.setGroup(group)
            group_title.text = "Группа ${group.number}"
        }
    }

}