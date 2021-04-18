package com.theevilroot.mybsuir.profile

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.*

class ProfileModel(
        api: ApiService,
        store: CredentialsStore,
        applicationContext: Context,
        cacheManager: ICacheManager
): ApiModel(api, store, applicationContext, cacheManager), IProfileModel {

    private var availableSkills: List<Skill>? = null

    override fun getPersonalCV(allowCache: Boolean): PersonalCV? {
        return apiCall(ApiService::personalCV, if (allowCache) cacheManager.personalCv() else null)
                ?.apply { cacheManager.personalCv(this) }
    }

    override fun getPersonalInformation(allowCache: Boolean): PersonalInformation? {
        return apiCall(ApiService::personalInformation, if (allowCache) cacheManager.personalInformation() else null)
                ?.apply { cacheManager.personalInformation(this) }
    }

    override fun updateAvailableSkills(allowCache: Boolean): List<Skill> {
        return apiCall(ApiService::availableSkills, if (allowCache) availableSkills else null).also {
            availableSkills = it
        } ?: emptyList()
    }

    override fun newSkill(data: NewSkill): Skill? {
        return apiCall({ api.newSkill(it, data) }, null)?.also { skill ->
            availableSkills = availableSkills?.let { it + skill } ?: emptyList()
        }
    }

    override fun addSkill(skill: Skill): Skill? {
        return apiCall({ api.addSkill(it, skill) }, null)
            ?.let { skill }
    }

    override fun updateSummary(text: String): String? {
        return apiCall({ api.summary(it, NewSummary(text)) }, null)
                ?.let { text }
    }

    override fun removeSkill(skill: Skill): Skill? {
        return apiCall({ api.removeSkill(it, skill) }, null)
                ?.let { skill }
    }

    override fun updateReferences(list: List<Reference>): List<Reference>? {
        return apiCall({ api.myReferences(it, list) }, null)
    }
}