package com.theevilroot.mybsuir.preferences

interface IPreferencesModel {

    fun savePreference(field: String, key: String, value: Boolean): Boolean?

}