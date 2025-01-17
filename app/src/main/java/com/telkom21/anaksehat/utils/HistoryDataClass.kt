package com.telkom21.anaksehat.utils

import android.os.Parcel
import android.os.Parcelable

data class HistoryDataClass(
    val tanggal: String = "",
    val berat: String? = null,
    val tinggi: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tanggal)
        parcel.writeString(berat)
        parcel.writeString(tinggi)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HistoryDataClass> {
        override fun createFromParcel(parcel: Parcel): HistoryDataClass {
            return HistoryDataClass(parcel)
        }

        override fun newArray(size: Int): Array<HistoryDataClass?> {
            return arrayOfNulls(size)
        }
    }
}
