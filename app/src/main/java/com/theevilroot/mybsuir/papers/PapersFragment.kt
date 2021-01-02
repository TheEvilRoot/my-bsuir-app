package com.theevilroot.mybsuir.papers

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.theevilroot.mybsuir.R
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
import java.net.UnknownHostException
import kotlin.math.abs

class PapersFragment : ModelDataFragment<PapersFragment.PapersViewState, List<Paper>>(R.layout.f_papers) {

    sealed class PapersViewState {

        abstract val loadingVisibility: Boolean

        object PapersLoading : PapersViewState() {
            override val loadingVisibility: Boolean = true
        }

        class PapersFailure(val message: String, val retry: View.() -> Unit) : PapersViewState() {
            override val loadingVisibility: Boolean = false

        }

        class PapersFilled (val papers: List<Paper>) : PapersViewState() {
            override val loadingVisibility: Boolean = false

        }
    }

    private val model by instance<PapersModel>()
    private val controller by lazy { PapersController(model) }
    private val papersAdapter by lazy { SimpleAdapter(R.layout.i_paper, ::PaperViewHolder) }

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
        updateData(true)
    }

    override fun getLoadingState(): PapersViewState =
            PapersViewState.PapersLoading

    override fun getFilledState(it: List<Paper>): PapersViewState =
            PapersViewState.PapersFilled(it)

    override fun getErrorState(msg: String, retryAction: View.() -> Unit): PapersViewState =
            PapersViewState.PapersFailure(msg, retryAction)

    override fun getDataUpdate(): Single<List<Paper>> =
            controller.updatePapers(true)

    override fun View.applyState(newState: PapersViewState) = with(newState) {
        papers_refresh.isRefreshing = loadingVisibility

        if (this is PapersViewState.PapersFilled) {
            papersAdapter.setData(papers)
        }
    }

}