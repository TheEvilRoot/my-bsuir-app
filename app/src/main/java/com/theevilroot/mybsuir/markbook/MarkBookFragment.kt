package com.theevilroot.mybsuir.markbook

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.common.visibility
import com.theevilroot.mybsuir.markbook.semesters.SemestersAdapter
import com.theevilroot.mybsuir.markbook.semesters.SemestersScrollDelegate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.f_markbook.view.*
import org.kodein.di.generic.instance
import java.net.UnknownHostException
import kotlin.math.max

class MarkBookFragment : BaseFragment(R.layout.f_markbook) {

    private sealed class MarkBookViewState {
        abstract val progressVisibility: Boolean
        abstract val markBookVisibility: Boolean
        abstract val errorViewVisibility: Boolean

        object MarkBookLoading: MarkBookViewState() {
            override val progressVisibility: Boolean = true
            override val markBookVisibility: Boolean = false
            override val errorViewVisibility: Boolean = false
        }
        class MarkBookFilled(val markBook: MarkBook): MarkBookViewState() {
            override val progressVisibility: Boolean = false
            override val markBookVisibility: Boolean = true
            override val errorViewVisibility: Boolean = false
        }
        class MarkBookFailed(val message: String, val retryHandler: View.() -> Unit): MarkBookViewState() {
            override val progressVisibility: Boolean = false
            override val markBookVisibility: Boolean = false
            override val errorViewVisibility: Boolean = true
        }
    }

    private val model: MarkBookModel by instance()
    private val cacheController: CacheController by instance()

    private val controller by lazy { MarkBookController(model) }

    private val semestersAdapter by lazy { SemestersAdapter() }

    override fun View.onView() {
        val snapHelper = PagerSnapHelper()
        with(mark_book_view) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = semestersAdapter
            snapHelper.attachToRecyclerView(this)
            addOnScrollListener(SemestersScrollDelegate(::onItemIndexChanged, ::onAlphaChanged))
        }

        updateMarkBook(true)
    }

    private fun onItemIndexChanged(index: Int) {
        semestersAdapter.data.getOrNull(index)?.let {
            view?.updateSemesterHeader(it)
        }
    }

    private fun onAlphaChanged(alpha: Float) = view?.run {
        markbook_title.alpha = alpha
        markbook_subtitle.alpha = alpha
        markbook_average_mark.alpha = alpha
    }

    private fun View.updateMarkBook(useCurrentCredentials: Boolean) {
        applyState(MarkBookViewState.MarkBookLoading)
        cacheController.preloadCacheAndCall(controller
                .updateMarkBook(false)
                .observeOn(AndroidSchedulers.mainThread()), useCurrentCredentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateMarkBookHandler(it) }){
                    updateMarkBookErrorHandler(it) }

    }

    private fun View.updateMarkBookHandler(it: MarkBook) {
        applyState(MarkBookViewState.MarkBookFilled(it))
    }

    private fun View.updateMarkBookErrorHandler(it: Throwable) {
        applyState(MarkBookViewState.MarkBookFailed(when (it) {
            is InternalException ->
                it.msg
            is NoCredentialsException ->
                return findNavController().navigate(R.id.fragment_login)
            is ReAuthRequiredException ->
                return updateMarkBook(false)
            is UnknownHostException ->
                context.getString(R.string.no_internet_error)
            else -> context.getString(R.string.unexpected_error,
                    it.javaClass.simpleName, it.localizedMessage)
        }) { view?.updateMarkBook(true) })
    }

    private fun View.updateSemesterHeader(item: Semester) {
        markbook_title.text = "${item.index} семестр"
        markbook_average_mark.text = String.format("%.2f", item.averageMark)
    }

    private fun View.applyState(newState: MarkBookViewState) = with(newState) {
        mark_book_view.visibility = markBookVisibility.visibility()
        markbook_average_mark_layout.visibility = markBookVisibility.visibility()
        markbook_subtitle.visibility = markBookVisibility.visibility()

        if (this is MarkBookViewState.MarkBookLoading)
            markbook_title.text = "Загрузка..."

        if (this is MarkBookViewState.MarkBookFilled) {
            semestersAdapter.setSemesters(markBook.semestersList)
            val index = mark_book_view.computeHorizontalScrollOffset() /
                    max(1, mark_book_view.computeHorizontalScrollExtent())
            semestersAdapter.data.getOrNull(index)?.let {
                updateSemesterHeader(it)
            }
        }
    }

}