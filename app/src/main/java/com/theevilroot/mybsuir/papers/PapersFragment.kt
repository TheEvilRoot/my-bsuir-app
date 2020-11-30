package com.theevilroot.mybsuir.papers

import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.data.Paper
import kotlinx.android.synthetic.main.f_papers.view.*
import kotlin.math.abs

class PapersFragment : BaseFragment(R.layout.f_papers) {

    sealed class PapersViewState {

        object PapersLoading : PapersViewState() {

        }

        class PapersFailure(val message: String, val retry: View.() -> Unit) : PapersViewState() {

        }

        class PapersFilled (val papers: List<Paper>) : PapersViewState() {

        }
    }

    override fun View.onView() {
        with(papers_app_bar) {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
                if (appBar.totalScrollRange == 0) return@OnOffsetChangedListener
                val value = abs(offset).toFloat() / appBar.totalScrollRange.toFloat()
                papers_header_layout.alpha = 1 - value
                papers_toolbar.alpha = if (value < 0.5) 0.0F else value / 0.5F - 1F
            })
        }
    }

    fun View.applyState(newState: PapersViewState) = with(newState) {

    }

}