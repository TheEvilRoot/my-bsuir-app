package com.theevilroot.mybsuir.announcements

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.announcements.holders.AnnouncementViewHolder
import com.theevilroot.mybsuir.common.adapters.ErrorAwareAdapter
import com.theevilroot.mybsuir.common.adapters.ErrorDescriptor
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.asVisibility
import com.theevilroot.mybsuir.common.data.Announcement
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_announcements.view.*
import org.kodein.di.generic.instance

class AnnouncementsFragment : ModelDataFragment<AnnouncementsFragment.AnnouncementsViewState, List<Announcement>>(R.layout.f_announcements) {

    sealed class AnnouncementsViewState {

        abstract val loadingVisibility: Boolean
        abstract val contentVisibility: Boolean

        object Loading : AnnouncementsViewState() {
            override val loadingVisibility: Boolean = true
            override val contentVisibility: Boolean = false
        }

        class Failure(val msg: String, @DrawableRes val image: Int, val retryAction: View.() -> Unit): AnnouncementsViewState() {
            override val loadingVisibility: Boolean = false
            override val contentVisibility: Boolean = true
        }

        class Success(val data: List<Announcement>) : AnnouncementsViewState() {
            override val loadingVisibility: Boolean = false
            override val contentVisibility: Boolean = true
        }

    }

    private val model by instance<IAnnouncementsModel>()
    private val controller by lazy { AnnouncementsController(model) }

    private val annsAdapter by lazy {
        ErrorAwareAdapter(R.layout.i_announcement, ::AnnouncementViewHolder)
    }

    override fun View.onView() {
        with(anns_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = annsAdapter
        }
        updateData(true)
    }

    override fun getLoadingState(): AnnouncementsViewState =
            AnnouncementsViewState.Loading

    override fun getFilledState(it: List<Announcement>): AnnouncementsViewState =
            AnnouncementsViewState.Success(it)

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): AnnouncementsViewState =
            AnnouncementsViewState.Failure(msg, getImageForError(it), retryAction)

    override fun getDataUpdate(): Single<List<Announcement>> =
            controller.updateAnnouncements(true)

    override fun View.applyState(newState: AnnouncementsViewState) = with(newState) {
        anns_list.visibility = contentVisibility.asVisibility()
        anns_subtitle.visibility = (!contentVisibility).asVisibility()
        anns_progress.visibility = loadingVisibility.asVisibility()

        if (this is AnnouncementsViewState.Loading) {
            anns_subtitle.text = "Загрузка..."
        }

        if (this is AnnouncementsViewState.Success) {
            annsAdapter.error = null
            annsAdapter.setData(data)
        }

        if (this is AnnouncementsViewState.Failure) {
            annsAdapter.error = ErrorDescriptor(
                    message = msg,
                    imageRes = image,
                    retryAction = retryAction
            )
        }

    }

}