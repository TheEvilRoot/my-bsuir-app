package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.group.IGroupModel
import com.theevilroot.mybsuir.markbook.IMarkBookModel
import com.theevilroot.mybsuir.papers.IPapersModel
import com.theevilroot.mybsuir.profile.IProfileModel

class SharedModel (
        private val profileModel: IProfileModel,
        private val papersModel: IPapersModel,
        private val groupModel: IGroupModel,
        private val markBookModel: IMarkBookModel
): IProfileModel, IPapersModel, IGroupModel, IMarkBookModel {

    private fun requireNotMe(c: Any) {
        if (c is SharedModel)
            throw RuntimeException("SharedModel cannot accumulate SharedModel")
    }

    init {
        requireNotMe(profileModel)
        requireNotMe(papersModel)
        requireNotMe(groupModel)
        requireNotMe(markBookModel)
    }

    override fun getPersonalCV(allowCache: Boolean): PersonalCV? {
        return profileModel.getPersonalCV(allowCache)
    }

    override fun getPersonalInformation(allowCache: Boolean): PersonalInformation? {
        return profileModel.getPersonalInformation(allowCache)
    }

    override fun getPapers(allowCache: Boolean): List<Paper>? {
        return papersModel.getPapers(allowCache)
    }

    override fun getPlaces(allowCache: Boolean): List<PaperPlaceCategory>? {
        return papersModel.getPlaces(allowCache)
    }

    override fun getGroupInfo(allowCache: Boolean): GroupInfo? {
        return groupModel.getGroupInfo(allowCache)
    }

    override fun getMarkBook(allowCache: Boolean): MarkBook? {
        return markBookModel.getMarkBook(allowCache)
    }
}