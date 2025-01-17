package com.telkom21.anaksehat.utils

import java.io.Serializable

data class UserChildDataClass(
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val umur: String? = null,
    val berat: String? = null,
    val tinggi: String? = null,
    val namaOrtu: String? = null,
    var parentId: String = ""
): Serializable {
    fun getFirstName(): String {
        // Split the name by space and return the first part
        return nama?.split(" ")?.firstOrNull() ?: ""
    }
}
