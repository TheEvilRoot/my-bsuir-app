package com.theevilroot.mybsuir.papers

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import com.theevilroot.mybsuir.papers.holders.PaperViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.f_papers.view.*
import org.kodein.di.generic.instance
import java.net.UnknownHostException
import kotlin.math.abs

class PapersFragment : BaseFragment(R.layout.f_papers) {

    private sealed class PapersViewState {

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
    private val cacheController by instance<CacheController>()
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
            setOnRefreshListener { updatePapers(false) }
        }
        updatePapers(true)
    }

    private fun View.updatePapers(useCurrentCredentials: Boolean) {
        applyState(PapersViewState.PapersLoading)
        cacheController.preloadCacheAndCall(controller
                .updatePapers(false)
                .observeOn(AndroidSchedulers.mainThread()), useCurrentCredentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ papersUpdateHandler(it) }) {
                    papersUpdateErrorHandler(it) }
    }

    private fun View.papersUpdateHandler(it: List<Paper>) =
            applyState(PapersViewState.PapersFilled(it))

    private fun View.papersUpdateErrorHandler(it: Throwable) {
        it.printStackTrace()
        applyState(
                PapersViewState.PapersFailure(when (it) {
                    is InternalException ->
                        it.msg
                    is NoCredentialsException ->
                        return findNavController().navigate(R.id.fragment_login)
                    is ReAuthRequiredException ->
                        return updatePapers(false)
                    is UnknownHostException ->
                        context.getString(R.string.no_internet_error)
                    else -> context.getString(R.string.unexpected_error,
                            it.javaClass.simpleName, it.localizedMessage)
                }) { view?.updatePapers(true) })
    }

    private fun View.applyState(newState: PapersViewState) = with(newState) {
        papers_refresh.isRefreshing = loadingVisibility

        if (this is PapersViewState.PapersFilled) {
            papersAdapter.setData(papers)
        }
    }

}