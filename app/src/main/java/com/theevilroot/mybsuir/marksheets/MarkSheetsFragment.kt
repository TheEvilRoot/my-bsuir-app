package com.theevilroot.mybsuir.marksheets

import android.graphics.Color
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.ErrorAwareAdapter
import com.theevilroot.mybsuir.common.adapters.ErrorDescriptor
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.data.MarkSheet
import com.theevilroot.mybsuir.marksheets.holders.MarkSheetViewHolder
import com.theevilroot.mybsuir.papers.PapersFragment
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_mark_sheets.view.*
import org.kodein.di.generic.instance
import kotlin.math.abs

class MarkSheetsFragment: ModelDataFragment<MarkSheetsFragment.MarkSheetsViewState, List<MarkSheet>>(R.layout.f_mark_sheets) {

    sealed class MarkSheetsViewState {

        abstract val loadingVisibility: Boolean

        object Loading: MarkSheetsViewState() {
            override val loadingVisibility: Boolean = true

        }
        class Filled(val data: List<MarkSheet>): MarkSheetsViewState() {
            override val loadingVisibility: Boolean = false

        }
        class Failure(@DrawableRes val image: Int, val message: String, val retryAction: View.() -> Unit): MarkSheetsViewState() {
            override val loadingVisibility: Boolean = false
        }
    }

    private val model: IMarkSheetsModel by instance()
    private val controller: MarkSheetsController by lazy { MarkSheetsController(model) }

    private val sheetsAdapter by lazy { ErrorAwareAdapter(R.layout.i_sheet, ::MarkSheetViewHolder) }

    override fun View.onView() {
        with(sheets_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = sheetsAdapter
        }
        with(sheets_appbar) {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
                if (appBar.totalScrollRange == 0) return@OnOffsetChangedListener
                val value = abs(offset).toFloat() / appBar.totalScrollRange.toFloat()
                sheets_header_layout.alpha = 1 - value
                sheets_toolbar.alpha = if (value < 0.5) 0.0F else value / 0.5F - 1F
            })
        }

        updateData(true)
    }

    override fun getLoadingState(): MarkSheetsViewState =
            MarkSheetsViewState.Loading

    override fun getFilledState(it: List<MarkSheet>): MarkSheetsViewState =
            MarkSheetsViewState.Filled(it)

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): MarkSheetsViewState =
            MarkSheetsViewState.Failure(this.getImageForError(it), msg, retryAction)

    override fun getDataUpdate(forceUpdate: Boolean): Single<List<MarkSheet>> {
        return controller.updateMarkBook(true)
    }

    override fun View.applyState(newState: MarkSheetsViewState) = with(newState) {
        sheets_refresh.isRefreshing = loadingVisibility

        if (this is MarkSheetsViewState.Filled)
            sheetsAdapter.setData(data)
        if (this is MarkSheetsViewState.Failure)
            sheetsAdapter.error = ErrorDescriptor (
                    title = "Ошибка",
                    message = message,
                    retryAction = retryAction,
                    imageRes = image,
                    backgroundColor = Color.rgb(0xf3, 0xf3, 0xf3))
    }
}