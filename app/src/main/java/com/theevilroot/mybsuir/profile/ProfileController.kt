package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileController (
    val model: ProfileModel
) {

    fun updateProfileInfo(): Single<ProfileInfo> =
        Single.create<ProfileInfo> {
            val personalCv = model.getPersonalCV()
                ?: throw InternalException("Невозможно получить данные профиля")
            val personalInformation = model.getPersonalInformation()
                ?: throw InternalException("Невозможно получить данные профиля")

            val profileInfo = ProfileInfo(
                personalInformation.name,
                personalCv.firstName,
                personalCv.birthDate,
                "студент ${personalCv.course} курса\nфакультета ${personalCv.faculty}\nспециальности ${personalCv.speciality}",
                (personalCv.rating * 2).toInt(),
                personalCv.photoUrl ?: "https://iis.bsuir.by/assets/default-photo.gif",
                personalInformation,
                personalCv
            )
            it.onSuccess(profileInfo)
        }.subscribeOn(Schedulers.io())

}