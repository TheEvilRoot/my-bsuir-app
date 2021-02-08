package com.theevilroot.mybsuir.announcements

import com.theevilroot.mybsuir.common.data.Announcement
import com.theevilroot.mybsuir.common.data.InternalException
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AnnouncementsController (
    val model: IAnnouncementsModel
) {

    fun updateAnnouncements(forceUpdate: Boolean): Single<List<Announcement>> =
            Single.create<List<Announcement>> {
                val data = model.getAnnouncements(allowCache = !forceUpdate)
                        ?: return@create it.onError(InternalException("Невозможно получить данные об объявдениях"))
                it.onSuccess(data)
            }.subscribeOn(Schedulers.io())
}