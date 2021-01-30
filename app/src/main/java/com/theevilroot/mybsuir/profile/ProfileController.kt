package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.SharedModel
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.MarkSheet
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.login.LoginModel
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
            emitter.onSuccess(sheets.count { it.status != MarkSheet.Status.PRINTED })
        }.subscribeOn(Schedulers.io())

}