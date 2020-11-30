package com.theevilroot.mybsuir.markbook.semesters

import androidx.recyclerview.widget.RecyclerView

class SemestersScrollDelegate (
    private val onItemIndexChanged: (Int) -> Unit,
    private val onAlphaChanged: (Float) -> Unit
) : RecyclerView.OnScrollListener() {

    private var markBookExtent = 0
    private var markBookTotalOffset = 0
    private var markBookIndex = -1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (markBookExtent == 0)
            markBookExtent = recyclerView.computeHorizontalScrollExtent()
        markBookTotalOffset += dx

        val localOffset = markBookTotalOffset % markBookExtent
        if (localOffset > 0) {
            val percent = localOffset.toFloat() / markBookExtent.toFloat()
            val alpha = if (percent < 0.5) {
                val idx = markBookTotalOffset / markBookExtent
                if (idx != markBookIndex) {
                    markBookIndex = idx
                    onItemIndexChanged(idx)
                }
                1 - (2 * percent)
            } else {
                val idx = (markBookTotalOffset / markBookExtent) + 1
                if (idx != markBookIndex) {
                    markBookIndex = idx
                    onItemIndexChanged(idx)
                }
                (2 * (percent - 0.5)).toFloat()
            }
            onAlphaChanged(alpha)
        }
    }

}