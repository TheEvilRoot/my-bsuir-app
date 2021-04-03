package com.theevilroot.mybsuir.schedule

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.data.ScheduleEntry
import com.theevilroot.mybsuir.schedule.adapters.ScheduleAdapter
import com.theevilroot.mybsuir.schedule.data.ScheduleItem
import com.theevilroot.mybsuir.schedule.holders.ScheduleItemViewHolder
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_home_schedule.view.*
import org.kodein.di.generic.instance

class HomeScheduleFragment : ModelDataFragment<HomeScheduleFragment.HomeScheduleViewState, DaySchedule>(R.layout.f_home_schedule) {

    sealed class HomeScheduleViewState {

        object Loading: HomeScheduleViewState()

        class Failed(val message: String, @DrawableRes val image: Int, val retryAction: View.() -> Unit): HomeScheduleViewState()

        class Filled(val data: DaySchedule): HomeScheduleViewState()

    }

    private val model: IScheduleModel by instance()
    private val controller by lazy { HomeScheduleController(model) }

    private val scheduleAdapter by lazy { ScheduleAdapter(
            resources.getString(R.string.schedule_start_of_day),
            resources.getString(R.string.schedule_end_of_day)) }

    override fun View.onView() {
        with(schedule_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = scheduleAdapter
        }

        updateData(true)
    }

    override fun getLoadingState(): HomeScheduleViewState =
        HomeScheduleViewState.Loading

    override fun getFilledState(it: DaySchedule): HomeScheduleViewState =
        HomeScheduleViewState.Filled(it)

    override fun getErrorState(
        it: Throwable,
        msg: String,
        retryAction: View.() -> Unit
    ): HomeScheduleViewState =
        HomeScheduleViewState.Failed(msg, getImageForError(it), retryAction)

    override fun getDataUpdate(forceUpdate: Boolean): Single<DaySchedule> =
        controller.updateTodaySchedule(true)

    override fun View.applyState(newState: HomeScheduleViewState) = with(newState) {
        if (this is HomeScheduleViewState.Filled) {
            scheduleAdapter.setSchedule(data.todaySchedules)
        }
    }

}