package com.theevilroot.mybsuir.markbook.semesters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.data.Semester
import com.theevilroot.mybsuir.markbook.marks.MarksAdapter

class SemestersAdapter : RecyclerView.Adapter<SemesterViewHolder>() {

    private var data: List<Semester> = emptyList()
    private val markAdapters = mutableListOf<MarksAdapter>()

    private fun newMarkAdapter(): MarksAdapter =
            MarksAdapter()

    fun setData(items: List<Semester>) {
        if (markAdapters.size < items.size) {
            markAdapters.addAll((0 until (items.size - markAdapters.size))
                    .map { newMarkAdapter() })
        }

        items.forEachIndexed { index, semester ->
            markAdapters[index].setData(semester.marks)
        }

        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SemesterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.i_markbook_semester, parent, false)
        return SemesterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: SemesterViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, markAdapters[item.index - 1])
    }

}

