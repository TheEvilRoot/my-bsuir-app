package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.data.*

interface IProfileModel {

    fun getPersonalCV(allowCache: Boolean): PersonalCV?

    fun getPersonalInformation(allowCache: Boolean): PersonalInformation?

    fun updateAvailableSkills(allowCache: Boolean): List<Skill>

    fun newSkill(data: NewSkill): Skill?

    fun addSkill(skill: Skill): Skill?

    fun updateSummary(text: String): String?

    fun removeSkill(skill: Skill): Skill?

    fun updateReferences(list: List<Reference>): List<Reference>?

}