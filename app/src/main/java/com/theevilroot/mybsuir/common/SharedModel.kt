package com.theevilroot.mybsuir.common

import android.content.Context
import com.theevilroot.mybsuir.announcements.AnnouncementsModel
import com.theevilroot.mybsuir.announcements.IAnnouncementsModel
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.group.GroupModel
import com.theevilroot.mybsuir.group.IGroupModel
import com.theevilroot.mybsuir.markbook.IMarkBookModel
import com.theevilroot.mybsuir.markbook.MarkBookModel
import com.theevilroot.mybsuir.marksheets.IMarkSheetsModel
import com.theevilroot.mybsuir.marksheets.MarkSheetsModel
import com.theevilroot.mybsuir.papers.IPapersModel
import com.theevilroot.mybsuir.papers.PapersModel
import com.theevilroot.mybsuir.preferences.IPreferencesModel
import com.theevilroot.mybsuir.preferences.PreferencesModel
import com.theevilroot.mybsuir.profile.IProfileModel
import com.theevilroot.mybsuir.profile.ProfileModel
import com.theevilroot.mybsuir.schedule.IScheduleModel
import com.theevilroot.mybsuir.schedule.ScheduleModel
import java.util.*

class SharedModel (
    apiService: ApiService, store: CredentialsStore, context: Context, cacheManager: ICacheManager
) : IProfileModel by ProfileModel(apiService, store, context, cacheManager),
    IPapersModel by PapersModel(apiService, store, context, cacheManager),
    IGroupModel by GroupModel(apiService, store, context, cacheManager),
    IMarkBookModel by MarkBookModel(apiService, store, context, cacheManager),
    IMarkSheetsModel by MarkSheetsModel(apiService, store, context, cacheManager),
    IAnnouncementsModel by AnnouncementsModel(apiService, store, context, cacheManager),
    IScheduleModel by ScheduleModel(apiService, store, context, cacheManager),
    IPreferencesModel by PreferencesModel(apiService, store, context, cacheManager)