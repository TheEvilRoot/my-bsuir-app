package com.theevilroot.mybsuir.papers

import android.graphics.Color
import android.view.View
import androidx.annotation.DrawableRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.ErrorAwareAdapter
import com.theevilroot.mybsuir.common.adapters.ErrorDescriptor
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import com.theevilroot.mybsuir.papers.holders.PaperViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_papers.view.*
import org.kodein.di.generic.instance
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.abs

class PapersFragment : ModelDataFragment<PapersFragment.PapersViewState, List<Paper>>(R.layout.f_papers) {

    sealed class PapersViewState {

        abstract val loadingVisibility: Boolean
        abstract val errorVisibility: Boolean

        object PapersLoading : PapersViewState() {
            override val loadingVisibility: Boolean = true
            override val errorVisibility: Boolean = false
        }

        class PapersFailure(@DrawableRes val image: Int, val message: String, val retry: View.() -> Unit) : PapersViewState() {
            override val loadingVisibility: Boolean = false
            override val errorVisibility: Boolean = true

        }

        class PapersFilled (val papers: List<Paper>) : PapersViewState() {
            override val loadingVisibility: Boolean = false
            override val errorVisibility: Boolean = false

        }
    }

    private val model by instance<IPapersModel>()
    private val controller by lazy { PapersController(model) }
    private val papersAdapter by lazy { ErrorAwareAdapter(R.layout.i_paper, ::PaperViewHolder) }

    override fun View.onView() {
        with(papers_app_bar) {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
                if (appBar.totalScrollRange == 0) return@OnOffsetChangedListener
                val value = abs(offset).toFloat() / appBar.totalScrollRange.toFloat()
                papers_header_layout.alpha = 1 - value
                papers_toolbar.alpha = if (value < 0.5) 0.0F else value / 0.5F - 1F
            })
        }
        with(papers_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = papersAdapter
        }
        with(papers_refresh) {
            setOnRefreshListener { updateData(true) }
        }
        with(get_paper_button) {
            setOnClickListener { findNavController().navigate(R.id.fragment_request_paper) }
        }
        updateData(true)
    }

    override fun getLoadingState(): PapersViewState =
            PapersViewState.PapersLoading

    override fun getFilledState(it: List<Paper>): PapersViewState =
            PapersViewState.PapersFilled(it)

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): PapersViewState =
            PapersViewState.PapersFailure(getImageForError(it), msg, retryAction)

    override fun getDataUpdate(): Single<List<Paper>> =
            controller.updatePapers(true)

    override fun View.applyState(newState: PapersViewState) = with(newState) {
        papers_refresh.isRefreshing = loadingVisibility
        if (!errorVisibility)
            papersAdapter.error = null

        if (this is PapersViewState.PapersFilled)
            papersAdapter.setData(papers)
        if (this is PapersViewState.PapersFailure)
            papersAdapter.error = ErrorDescriptor (
                    title = "Ошибка",
                    message = message,
                    retryAction = retry,
                    imageRes = image,
                    backgroundColor = Color.rgb(0xf2, 0xee, 0xcb))
    }

}