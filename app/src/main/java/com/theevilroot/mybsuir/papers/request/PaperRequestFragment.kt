package com.theevilroot.mybsuir.papers.request

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.flexbox.*
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.MonoHolderChoiceAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.utils.asVisibility
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.data.PaperPlace
import com.theevilroot.mybsuir.common.data.PaperPlaceCategory
import com.theevilroot.mybsuir.papers.IPapersModel
import com.theevilroot.mybsuir.papers.PapersController
import com.theevilroot.mybsuir.papers.request.holders.PaperPlaceCategoryViewHolder
import com.theevilroot.mybsuir.papers.request.holders.PaperPlaceViewHolder
import com.theevilroot.mybsuir.papers.request.holders.PaperTypeViewHolder
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_paper_request.*
import kotlinx.android.synthetic.main.f_paper_request.view.*
import org.kodein.di.generic.instance

class PaperRequestFragment : ModelDataFragment<PaperRequestFragment.PaperRequestViewState, List<PaperPlaceCategory>>(R.layout.f_paper_request) {

    sealed class PaperRequestViewState {

        abstract val loadingVisibility: Boolean
        abstract val destinationVisibility: Boolean
        abstract val typeChooserAvailability: Boolean
        abstract val actionAvailability: Boolean
        abstract val actionText: String?

        object LoadingState : PaperRequestViewState() {
            override val loadingVisibility: Boolean = true
            override val destinationVisibility: Boolean = false
            override val actionAvailability: Boolean = false
            override val actionText: String? = null
            override val typeChooserAvailability: Boolean = false
        }

        class PayloadState (
            val type: Paper.Type,
            val category: PaperPlaceCategory?,
            val place: PaperPlace? = null,
            val userPlace: String? = null
        ) : PaperRequestViewState() {
            override val loadingVisibility: Boolean = false
            override val destinationVisibility: Boolean = true
            override val typeChooserAvailability: Boolean = true
            override val actionText: String? = null
            override val actionAvailability: Boolean = category != null && ((place != null) xor (userPlace != null))

            operator fun plus(newType: Paper.Type): PayloadState =
                    PayloadState(newType, category, place, userPlace)

            operator fun plus(newCategory: PaperPlaceCategory): PayloadState =
                    PayloadState(type, newCategory, null, null)

            operator fun plus(place: PaperPlace?): PayloadState =
                    PayloadState(type, category, place, null)

            operator fun plus(userPlace: String): PayloadState =
                    PayloadState(type, category, null, userPlace)
        }

        class FilledState (val list: List<PaperPlaceCategory>) : PaperRequestViewState() {
            override val loadingVisibility: Boolean = false
            override val destinationVisibility: Boolean = true
            override val actionAvailability: Boolean = true
            override val actionText: String? = null
            override val typeChooserAvailability: Boolean = true
        }

        class ErrorState (val message: String) : PaperRequestViewState() {
            override val loadingVisibility: Boolean = false
            override val destinationVisibility: Boolean = false
            override val actionAvailability: Boolean = false
            override val actionText: String? = null
            override val typeChooserAvailability: Boolean = false
        }
    }

    private val model by instance<IPapersModel>()
    private val controller by lazy { PapersController(model) }

    private val typeAdapter by lazy {
        MonoHolderChoiceAdapter(R.layout.i_choose_simple_item, ::PaperTypeViewHolder,
                this::onTypeSelected)
    }

    private val destinationCategoryAdapter by lazy {
        val adapter = MonoHolderChoiceAdapter(R.layout.i_choose_simple_item, ::PaperPlaceCategoryViewHolder,
                this::onDestinationCategorySelected)
        adapter
    }

    private val destinationAdapter by lazy {
        val adapter = MonoHolderChoiceAdapter(R.layout.i_choose_simple_item, ::PaperPlaceViewHolder,
        this::onDestinationSelected)
        adapter
    }

    private var currentPayload: PaperRequestViewState.PayloadState? = null

    override fun View.onView() {
        with(paper_type_chooser) {
            adapter = typeAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.NOWRAP
                justifyContent = JustifyContent.CENTER
            }
            (itemAnimator as? SimpleItemAnimator)
                    ?.supportsChangeAnimations = false
            typeAdapter.setData(Paper.Type.values().toList())
            typeAdapter.select(0)
            typeAdapter.allowUnSelection = false
        }
        with(paper_destination_category_chooser) {
            adapter = destinationCategoryAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.NOWRAP
                justifyContent = JustifyContent.CENTER
            }
            (itemAnimator as? SimpleItemAnimator)
                    ?.supportsChangeAnimations = false

        }
        with(paper_destination_chooser) {
            adapter = destinationAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.SPACE_BETWEEN
            }
            (itemAnimator as? SimpleItemAnimator)
                    ?.supportsChangeAnimations = false
        }

        updateData(true)
    }

    override fun getLoadingState(): PaperRequestViewState =
            PaperRequestViewState.LoadingState

    override fun getDataUpdate(forceUpdate: Boolean): Single<List<PaperPlaceCategory>> =
            controller.updatePlaces(forceUpdate)

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): PaperRequestViewState =
            PaperRequestViewState.ErrorState("Error: $msg")

    override fun getFilledState(it: List<PaperPlaceCategory>): PaperRequestViewState =
            PaperRequestViewState.FilledState(it)

    override fun View.applyState(newState: PaperRequestViewState) = with(newState) {
        paper_request_filled.visibility = destinationVisibility.asVisibility()
        paper_request_progress.visibility = loadingVisibility.asVisibility()

        paper_request_submit.isEnabled = actionAvailability
        typeAdapter.allowInteraction = typeChooserAvailability

        if (actionText != null)
            paper_request_submit.text = actionText

        if (this is PaperRequestViewState.FilledState) {
            destinationCategoryAdapter.setData(list)
            destinationCategoryAdapter.select(0)
            destinationCategoryAdapter.allowUnSelection = false

            repairPayloadState()
        }

        if (this is PaperRequestViewState.ErrorState) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        if (this is PaperRequestViewState.PayloadState) {
            paper_request_summary.text = getString(
                    R.string.paper_request_summary,
                    type.sealSummaryName,
                    userPlace ?: place?.name ?: "?")
            currentPayload = this // anti-pattern but i don't give a fuck
        }
    }

    private fun repairPayloadState() {
        currentPayload?.let { view?.applyState(it) }
    }

    private fun onTypeSelected(index: Int, type: Paper.Type?) {
        paper_type_hint.setText(type?.hintRes ?: R.string.paper_type_all_hint)
        type ?: return

        val newPayload = currentPayload?.let { it + type }
                ?: PaperRequestViewState.PayloadState(type, null)
        view?.applyState(newPayload)
    }

    private fun onDestinationCategorySelected(index: Int, category: PaperPlaceCategory?) {
        destinationAdapter.setData(category?.places ?: emptyList())
        category ?: return

        currentPayload?.let {
            view?.applyState(it + category)
        }
    }

    private fun onDestinationSelected(index: Int, place: PaperPlace?) {
        currentPayload?.let {
            view?.applyState(it + place)
        }
    }

}