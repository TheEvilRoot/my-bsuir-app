package com.theevilroot.mybsuir.papers.request

import android.view.View
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.papers.PapersController
import com.theevilroot.mybsuir.papers.PapersModel
import org.kodein.di.generic.instance

class PaperRequestFragment : BaseFragment<PaperRequestFragment.PaperRequestViewState>(R.layout.f_paper_request) {

    sealed class PaperRequestViewState {

        abstract val loadingVisibility: Boolean

        object LoadingState : PaperRequestViewState() {
            override val loadingVisibility: Boolean = true
        }

        class PayloadState (val message: String, val action: View.() -> Unit) : PaperRequestViewState() {
            override val loadingVisibility: Boolean = false
        }
    }

    private val model by instance<PapersModel>()
    private val controller by lazy { PapersController(model) }

    override fun View.applyState(newState: PaperRequestViewState) = with(newState) {

    }

}