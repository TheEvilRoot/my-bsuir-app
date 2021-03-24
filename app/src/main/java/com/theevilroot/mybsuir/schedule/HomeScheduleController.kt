package com.theevilroot.mybsuir.schedule

import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.data.InternalException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeScheduleController (
    val model: IScheduleModel
) {

    fun updateTodaySchedule(forceUpdate: Boolean): Single<DaySchedule> =
        Single.create<DaySchedule> {
            val schedule = model.getDaySchedule(allowCache = !forceUpdate)
                ?: return@create it.onError(InternalException("Невозможно получить о расписании"))

            it.onSuccess(schedule)
        }.subscribeOn(Schedulers.io())

}