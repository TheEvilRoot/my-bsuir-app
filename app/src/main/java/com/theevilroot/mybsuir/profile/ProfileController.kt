package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.SharedModel
import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileController (
    val model: SharedModel
) {

    fun updateProfileInfo(forceUpdate: Boolean): Single<ProfileInfo> =
        Single.create<ProfileInfo> {
            val personalCv = model.getPersonalCV(allowCache = !forceUpdate)
                ?: return@create it.onError(InternalException("Невозможно получить данные профиля"))
            val personalInformation = model.getPersonalInformation(allowCache = !forceUpdate)
                ?: return@create it.onError(InternalException("Невозможно получить данные профиля"))

            val profileInfo = ProfileInfo(
                personalInformation.name,
                personalCv.firstName,
                personalCv.birthDate,
                "студент ${personalCv.course} курса\nфакультета ${personalCv.faculty}\nспециальности ${personalCv.speciality}",
                (personalCv.rating * 2).toInt(),
                personalCv.photoUrl ?: "https://iis.bsuir.by/assets/default-photo.gif",
                personalCv.skills,
                personalCv.summary,
                personalCv.references,
                personalInformation,
                personalCv
            )
            it.onSuccess(profileInfo)
        }.subscribeOn(Schedulers.io())

    fun updatePapersCount(): Single<Int> =
        Single.create<Int> { emitter ->
            val papers = model.getPapers(true)
                ?: return@create emitter.onSuccess(0)
            emitter.onSuccess(papers.count { it.status() != Paper.Status.PRINTED })
        }.subscribeOn(Schedulers.io())

    fun updateSheetsCount(): Single<Int> =
        Single.create<Int> { emitter ->
            val sheets = model.getMarkSheets(true)
                    ?: return@create emitter.onSuccess(0)
            emitter.onSuccess(sheets.count { it.status() != MarkSheet.Status.PRINTED })
        }.subscribeOn(Schedulers.io())

    fun suggestSkills(text: String, strict: Boolean): Single<List<Skill>> =
            Single.create<List<Skill>> { emitter ->
                val data = model.updateAvailableSkills(true)
                emitter.onSuccess(data.filter {
                    if (strict)
                        it.name.toLowerCase().startsWith(text.toLowerCase())
                    else it.name.toLowerCase().contains(text.toLowerCase())
                })
            }.subscribeOn(Schedulers.io())

    fun submitSkill(skillName: String): Completable =
        Completable.create {
            val data = model.updateAvailableSkills(true)
            val skill = skillName.trim()
            val available = data.firstOrNull { it.name.equals(skill, ignoreCase=true) }
                ?: model.newSkill(NewSkill(skill))
                ?: return@create it.onError(InternalException("Неудалось добавить новый навык"))

            model.addSkill(available)
            it.onComplete()
        }.subscribeOn(Schedulers.io())

    fun updateSummary(text: String): Completable =
            Completable.create {
                model.updateSummary(text)
                it.onComplete()
            }.subscribeOn(Schedulers.io())

    fun removeSkill(skill: Skill): Completable =
            Completable.create {
                model.removeSkill(skill)
                it.onComplete()
            }.subscribeOn(Schedulers.io())
}