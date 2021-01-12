package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation

interface IProfileModel {

    fun getPersonalCV(allowCache: Boolean): PersonalCV?

    fun getPersonalInformation(allowCache: Boolean): PersonalInformation?

}