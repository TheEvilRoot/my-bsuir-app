package com.theevilroot.mybsuir.papers.request

import android.view.View
import com.google.android.flexbox.*
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.ChoiceAdapter
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.adapters.SimpleViewHolder
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.papers.PapersController
import com.theevilroot.mybsuir.papers.PapersModel
import com.theevilroot.mybsuir.papers.request.holders.PaperTypeViewHolder
import kotlinx.android.synthetic.main.f_paper_request.*
import kotlinx.android.synthetic.main.f_paper_request.view.*
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

    private val typeAdapter: ChoiceAdapter<Paper.Type, PaperTypeViewHolder, PaperTypeViewHolder, PaperTypeViewHolder> by lazy {
        ChoiceAdapter(R.layout.i_paper_type, R.layout.i_paper_type,
            ::PaperTypeViewHolder, ::PaperTypeViewHolder, this::onTypeSelected)
    }

    override fun View.onView() {
        paper_type_chooser.adapter = typeAdapter
        paper_type_chooser.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.NOWRAP
            justifyContent = JustifyContent.CENTER
        }
        typeAdapter.setData(Paper.Type.values().toList())
    }

    override fun View.applyState(newState: PaperRequestViewState) = with(newState) {

    }

    private fun onTypeSelected(index: Int, type: Paper.Type?) {
        paper_type_hint.setText(type?.hintRes ?: R.string.paper_type_all_hint)
    }

}