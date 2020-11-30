package com.theevilroot.mybsuir.markbook.semesters

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.common.data.Semester
import com.theevilroot.mybsuir.markbook.marks.MarksAdapter
import kotlinx.android.synthetic.main.i_markbook_semester.view.*

class SemesterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(data: Semester, marksAdapter: MarksAdapter) = with(itemView) {
        with(marks_view) {
            if (layoutManager !is LinearLayoutManager)
                layoutManager = LinearLayoutManager(context)
            adapter = marksAdapter
            adapter?.notifyDataSetChanged()
        }
    }
}