package com.theevilroot.mybsuir.common

import android.content.Context
import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.group.GroupModel
import com.theevilroot.mybsuir.group.IGroupModel
import com.theevilroot.mybsuir.markbook.IMarkBookModel
import com.theevilroot.mybsuir.markbook.MarkBookModel
import com.theevilroot.mybsuir.marksheets.IMarkSheetsModel
import com.theevilroot.mybsuir.marksheets.MarkSheetsModel
import com.theevilroot.mybsuir.papers.IPapersModel
import com.theevilroot.mybsuir.papers.PapersModel
import com.theevilroot.mybsuir.profile.IProfileModel
import com.theevilroot.mybsuir.profile.ProfileModel

class SharedModel (
        private val profileModel: IProfileModel,
        private val papersModel: IPapersModel,
        private val groupModel: IGroupModel,
        private val markBookModel: IMarkBookModel,
        private val markSheetsModel: IMarkSheetsModel
): IProfileModel, IPapersModel, IGroupModel, IMarkBookModel, IMarkSheetsModel {

    private fun requireNotMe(c: Any) {
        if (c is SharedModel)
            throw RuntimeException("SharedModel cannot accumulate SharedModel")
    }

    init {
        requireNotMe(profileModel)
        requireNotMe(papersModel)
        requireNotMe(groupModel)
        requireNotMe(markBookModel)
        requireNotMe(markSheetsModel)
    }

    constructor(apiService: ApiService, store: CredentialsStore, context: Context): this(
            ProfileModel(apiService, store, context),
            PapersModel(apiService, store, context),
            GroupModel(apiService, store, context),
            MarkBookModel(apiService, store, context),
            MarkSheetsModel(apiService, store, context)
    )

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

    override fun getMarkSheets(allowCache: Boolean): List<MarkSheet>? {
        return markSheetsModel.getMarkSheets(allowCache)
    }
}