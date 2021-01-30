package com.theevilroot.mybsuir.marksheets

import com.theevilroot.mybsuir.common.data.MarkSheet

interface IMarkSheetsModel {

    fun getMarkSheets(allowCache: Boolean): List<MarkSheet>?

}