package com.theevilroot.mybsuir.markbook

import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.MarkBook
import io.reactivex.rxjava3.core.Single

class MarkBookController (
        val model: MarkBookModel
) {

    fun updateMarkBook(forceUpdate: Boolean): Single<MarkBook> =
            Single.create<MarkBook> {
                val markBook = model.getMarkBook(allowCache = !forceUpdate)
                        ?: return@create it.onError(InternalException("Невозможно получить данные зачетки"))

                it.onSuccess(markBook)
            }

}