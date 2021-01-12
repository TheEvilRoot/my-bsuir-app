package com.theevilroot.mybsuir.markbook

import com.theevilroot.mybsuir.common.data.MarkBook

interface IMarkBookModel {

    fun getMarkBook(allowCache: Boolean): MarkBook?

}