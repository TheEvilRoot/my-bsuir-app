package com.theevilroot.mybsuir.papers

import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.Paper
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class PapersController (val model: PapersModel) {

    fun updatePapers(forceUpdate: Boolean): Single<List<Paper>> =
        Single.create<List<Paper>> {
            val papers = model.getPapers(allowCache = !forceUpdate)
                ?: return@create it.onError(InternalException("Невозможно получить данные о справках"))

            it.onSuccess(papers)
        }.subscribeOn(Schedulers.io())

}