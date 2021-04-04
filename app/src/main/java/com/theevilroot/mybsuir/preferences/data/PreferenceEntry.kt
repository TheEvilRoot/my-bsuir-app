package com.theevilroot.mybsuir.preferences.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PreferenceEntry (
    val type: PreferenceType,
    @StringRes val name: Int,
    @DrawableRes val activeIcon: Int,
    @DrawableRes val inactiveIcon: Int,
    val value: Boolean,
    @StringRes val valueActive: Int,
    @StringRes val valueInactive: Int
) {

    fun toggled(): PreferenceEntry =
        PreferenceEntry(type, name, activeIcon, inactiveIcon, !value, valueActive, valueInactive)

}