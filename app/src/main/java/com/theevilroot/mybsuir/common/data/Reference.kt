package com.theevilroot.mybsuir.common.data

import android.os.Parcel
import android.os.Parcelable

data class Reference(
    val id: Int?,
    val name: String,
    val reference: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            id = parcel.readValue(Int::class.java.classLoader) as? Int,
            name = parcel.readString() ?: "",
            reference = parcel.readString() ?: "") {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
        dest.writeString(reference)
    }

    companion object CREATOR : Parcelable.Creator<Reference> {
        override fun createFromParcel(parcel: Parcel): Reference {
            return Reference(parcel)
        }

        override fun newArray(size: Int): Array<Reference?> {
            return arrayOfNulls(size)
        }
    }

}