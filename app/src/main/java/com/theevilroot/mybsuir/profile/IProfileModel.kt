package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation
import com.theevilroot.mybsuir.common.data.Skill
import com.theevilroot.mybsuir.common.data.NewSkill

interface IProfileModel {

    fun getPersonalCV(allowCache: Boolean): PersonalCV?

    fun getPersonalInformation(allowCache: Boolean): PersonalInformation?

    fun updateAvailableSkills(allowCache: Boolean): List<Skill>

    fun newSkill(data: NewSkill): Skill?

    fun addSkill(skill: Skill): Skill?

    fun updateSummary(text: String): String?

    fun removeSkill(skill: Skill): Skill?

}