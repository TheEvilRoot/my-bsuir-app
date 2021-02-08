package com.theevilroot.mybsuir.announcements

import com.theevilroot.mybsuir.common.data.Announcement

interface IAnnouncementsModel {

    fun getAnnouncements(allowCache: Boolean): List<Announcement>?

}