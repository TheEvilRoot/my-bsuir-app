package com.theevilroot.mybsuir.papers

import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.data.PaperPlaceCategory

interface IPapersModel {

    fun getPapers(allowCache: Boolean): List<Paper>?

    fun getPlaces(allowCache: Boolean): List<PaperPlaceCategory>?

}