package com.theevilroot.mybsuir.marksheets

import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.MarkSheet
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MarkSheetsController(
        val model: IMarkSheetsModel,
) {

    fun updateMarkBook(forceUpdate: Boolean): Single<List<MarkSheet>> =
            Single.create<List<MarkSheet>> {
                val markSheets = model.getMarkSheets(allowCache = !forceUpdate)
                        ?: return@create it.onError(InternalException("Невозможно получить данные о ведомостях"))
                it.onSuccess(markSheets)
            }.subscribeOn(Schedulers.io())

}