package com.telkom21.anaksehat.utils

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ChildDataClass(
    var childID: String = "",
    val id: String = "",
    var nama: String? = null,
    var umur: String? = null,
    var berat: String? = null,
    var tinggi: String? = null,
    val jenisKelamin: String? = null,
    val namaOrtu: String = "",
    var parentId: String = "",
    val history: Map<String, HistoryDataClass>? = null
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("history")
    )
    fun getFirstName(): String {
        // Split the name by space and return the first part
        return nama?.split(" ")?.firstOrNull() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(childID)
        parcel.writeString(id)
        parcel.writeString(nama)
        parcel.writeString(umur)
        parcel.writeString(berat)
        parcel.writeString(tinggi)
        parcel.writeString(jenisKelamin)
        parcel.writeString(namaOrtu)
        parcel.writeString(parentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChildDataClass> {
        override fun createFromParcel(parcel: Parcel): ChildDataClass {
            return ChildDataClass(parcel)
        }

        override fun newArray(size: Int): Array<ChildDataClass?> {
            return arrayOfNulls(size)
        }
    }
}

