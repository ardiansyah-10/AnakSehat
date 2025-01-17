package com.telkom21.anaksehat.utils

data class IMTReference(
    val umur: Int,
    val minus3SD: Double,
    val minus2SD: Double,
    val minus1SD: Double,
    val median: Double,
    val plus1SD: Double,
    val plus2SD: Double,
    val plus3SD: Double

)

val imtRefLaki = listOf(
    IMTReference(0, 10.2, 11.1, 12.1, 13.2, 14.5, 16.0, 17.7),
    IMTReference(1, 10.9, 12.0, 13.1, 14.4, 15.9, 17.4, 19.1),
    IMTReference(2, 12.5, 13.7, 15.0, 16.3, 17.8, 19.4, 21.1),
    IMTReference(3, 13.1, 14.3, 15.5, 16.9, 18.4, 20.0, 21.8),
    IMTReference(4, 13.4, 14.5, 15.8, 17.2, 18.7, 20.3, 22.1),
    IMTReference(5, 13.5, 14.7, 15.9, 17.3, 18.8, 20.5, 22.3),
    IMTReference(6, 13.6, 14.7, 16.0, 17.3, 18.8, 20.5, 22.3),
    IMTReference(7, 13.7, 14.8, 16.0, 17.3, 18.8, 20.5, 22.3),
    IMTReference(8, 13.6, 14.7, 15.9, 17.3, 18.7, 20.4, 22.2),
    IMTReference(9, 13.6, 14.7, 15.8, 17.2, 18.6, 20.3, 22.1),
    IMTReference(10, 13.5, 14.6, 15.7, 17.0, 18.5, 20.1, 22.0),
    IMTReference(11, 13.4, 14.5, 15.6, 16.9, 18.4, 20.0, 21.8),
    IMTReference(12, 13.4, 14.4, 15.5, 16.8, 18.2, 19.8, 21.6),
    IMTReference(13, 13.3, 14.3, 15.4, 16.7, 18.1, 19.7, 21.5),
    IMTReference(14, 13.2, 14.2, 15.3, 16.6, 18.0, 19.5, 21.3),
    IMTReference(15, 13.1, 14.1, 15.2, 16.4, 17.8, 19.4, 21.2),
    IMTReference(16, 13.1, 14.0, 15.1, 16.3, 17.7, 19.3, 21.0),
    IMTReference(17, 13.0, 13.9, 15.0, 16.2, 17.6, 19.1, 20.9),
    IMTReference(18, 12.9, 13.9, 14.9, 16.1, 17.5, 19.0, 20.8),
    IMTReference(19, 12.9, 13.8, 14.9, 16.1, 17.4, 18.9, 20.7),
    IMTReference(20, 12.8, 13.7, 14.8, 16.0, 17.3, 18.8, 20.6),
    IMTReference(21, 12.8, 13.7, 14.7, 15.9, 17.2, 18.7, 20.5),
    IMTReference(22, 12.7, 13.6, 14.7, 15.8, 17.2, 18.7, 20.4),
    IMTReference(23, 12.7, 13.6, 14.6, 15.8, 17.1, 18.6, 20.3),
    IMTReference(24, 12.7, 13.6, 14.6, 15.7, 17.0, 18.5, 20.3),
    IMTReference(25, 12.8, 13.8, 14.8, 16.0, 17.3, 18.8, 20.5),
    IMTReference(26, 12.8, 13.7, 14.8, 15.9, 17.3, 18.8, 20.5),
    IMTReference(27, 12.7, 13.7, 14.7, 15.9, 17.2, 18.7, 20.4),
    IMTReference(28, 12.7, 13.6, 14.7, 15.9, 17.2, 18.7, 20.4),
    IMTReference(29, 12.7, 13.6, 14.7, 15.8, 17.1, 18.6, 20.3),
    IMTReference(30, 12.6, 13.6, 14.6, 15.8, 17.1, 18.6, 20.2),
    IMTReference(31, 12.6, 13.5, 14.6, 15.8, 17.1, 18.6, 20.2),
    IMTReference(32, 12.5, 13.5, 14.6, 15.7, 17.0, 18.5, 20.1),
    IMTReference(33, 12.5, 13.5, 14.5, 15.7, 17.0, 18.5, 20.1),
    IMTReference(34, 12.5, 13.4, 14.5, 15.7, 17.0, 18.4, 20.1),
    IMTReference(35, 12.4, 13.4, 14.5, 15.6, 16.9, 18.4, 20.0),
    IMTReference(36, 12.4, 13.4, 14.4, 15.6, 16.9, 18.4, 20.0),
    IMTReference(37, 12.4, 13.3, 14.4, 15.6, 16.9, 18.3, 19.9),
    IMTReference(38, 12.3, 13.3, 14.4, 15.5, 16.8, 18.3, 19.9),
    IMTReference(39, 12.3, 13.3, 14.3, 15.5, 16.8, 18.3, 19.9),
    IMTReference(40, 12.3, 13.3, 14.3, 15.5, 16.8, 18.2, 19.9),
    IMTReference(41, 12.2, 13.2, 14.3, 15.5, 16.8, 18.2, 19.9),
    IMTReference(42, 12.2, 13.2, 14.3, 15.4, 16.8, 18.2, 19.9),
    IMTReference(43, 12.2, 13.2, 14.2, 15.4, 16.7, 18.2, 19.8),
    IMTReference(44, 12.2, 13.1, 14.2, 15.4, 16.7, 18.2, 19.8),
    IMTReference(45, 12.2, 13.1, 14.2, 15.4, 16.7, 18.2, 19.8),
    IMTReference(46, 12.1, 13.1, 14.2, 15.4, 16.7, 18.2, 19.8),
    IMTReference(47, 12.1, 13.1, 14.2, 15.3, 16.7, 18.2, 19.9),
    IMTReference(48, 12.1, 13.1, 14.1, 15.3, 16.7, 18.2, 19.9),
    IMTReference(49, 12.1, 13.0, 14.1, 15.3, 16.7, 18.2, 19.9),
    IMTReference(50, 12.1, 13.0, 14.1, 15.3, 16.7, 18.2, 19.9),
    IMTReference(51, 12.1, 13.0, 14.1, 15.3, 16.7, 18.2, 19.9),
    IMTReference(52, 12.0, 13.0, 14.1, 15.3, 16.6, 18.2, 19.9),
    IMTReference(53, 12.0, 13.0, 14.1, 15.3, 16.6, 18.2, 20.0),
    IMTReference(54, 12.0, 13.0, 14.0, 15.3, 16.6, 18.2, 20.0),
    IMTReference(55, 12.0, 13.0, 14.0, 15.2, 16.6, 18.2, 20.0),
    IMTReference(56, 12.0, 12.9, 14.0, 15.2, 16.6, 18.2, 20.1),
    IMTReference(57, 12.0, 12.9, 14.0, 15.2, 16.6, 18.2, 20.1),
    IMTReference(58, 12.0, 12.9, 14.0, 15.2, 16.6, 18.3, 20.2),
    IMTReference(59, 12.0, 12.9, 14.0, 15.2, 16.6, 18.3, 20.2),
    IMTReference(60, 12.0, 12.9, 14.0, 15.2, 16.6, 18.3, 20.3)
)

val imtRefPer = listOf(
    IMTReference(0, 10.1, 11.1, 12.2, 13.3, 14.6, 16.1, 17.7),
    IMTReference(1, 10.8, 12.0, 13.2, 14.6, 16.0, 17.5, 19.1),
    IMTReference(2, 11.8, 13.0, 14.3, 15.8, 17.3, 19.0, 20.7),
    IMTReference(3, 12.4, 13.6, 14.9, 16.4, 17.9, 19.7, 21.5),
    IMTReference(4, 12.7, 13.9, 15.2, 16.7, 18.3, 20.0, 22.0),
    IMTReference(5, 12.9, 14.1, 15.4, 16.8, 18.4, 20.2, 22.2),
    IMTReference(6, 13.0, 14.1, 15.5, 16.9, 18.5, 20.3, 22.3),
    IMTReference(7, 13.0, 14.2, 15.5, 16.9, 18.5, 20.3, 22.3),
    IMTReference(8, 13.0, 14.1, 15.4, 16.8, 18.4, 20.2, 22.2),
    IMTReference(9, 12.9, 14.1, 15.3, 16.7, 18.3, 20.1, 22.1),
    IMTReference(10, 12.9, 14.0, 15.2, 16.6, 18.2, 19.9, 21.9),
    IMTReference(11, 12.8, 13.9, 15.1, 16.5, 18.0, 19.8, 21.8),
    IMTReference(12, 12.7, 13.8, 15.0, 16.4, 17.9, 19.6, 21.6),
    IMTReference(13, 12.6, 13.7, 14.9, 16.2, 17.7, 19.5, 21.4),
    IMTReference(14, 12.6, 13.6, 14.8, 16.1, 17.6, 19.3, 21.3),
    IMTReference(15, 12.5, 13.5, 14.7, 16.0, 17.5, 19.2, 21.1),
    IMTReference(16, 12.4, 13.5, 14.6, 15.9, 17.4, 19.1, 21.0),
    IMTReference(17, 12.4, 13.4, 14.5, 15.8, 17.3, 18.9, 20.9),
    IMTReference(18, 12.3, 13.3, 14.4, 15.7, 17.2, 18.8, 20.8),
    IMTReference(19, 12.3, 13.2, 14.3, 15.7, 17.1, 18.8, 20.7),
    IMTReference(20, 12.2, 13.2, 14.3, 15.6, 17.0, 18.7, 20.6),
    IMTReference(21, 12.2, 13.2, 14.3, 15.5, 17.0, 18.6, 20.5),
    IMTReference(22, 12.2, 13.1, 14.2, 15.5, 16.9, 18.5, 20.4),
    IMTReference(23, 12.2, 13.1, 14.2, 15.4, 16.9, 18.5, 20.4),
    IMTReference(24, 12.1, 13.1, 14.2, 15.4, 16.8, 18.4, 20.3),
    IMTReference(25, 12.4, 13.3, 14.4, 15.7, 17.1, 18.7, 20.6),
    IMTReference(26, 12.3, 13.3, 14.4, 15.6, 17.0, 18.7, 20.6),
    IMTReference(27, 12.3, 13.3, 14.4, 15.6, 17.0, 18.6, 20.5),
    IMTReference(28, 12.3, 13.3, 14.3, 15.6, 17.0, 18.6, 20.4),
    IMTReference(29, 12.3, 13.2, 14.3, 15.6, 17.0, 18.6, 20.4),
    IMTReference(30, 12.3, 13.2, 14.3, 15.5, 16.9, 18.5, 20.4),
    IMTReference(31, 12.2, 13.2, 14.3, 15.5, 16.9, 18.5, 20.4),
    IMTReference(32, 12.2, 13.2, 14.3, 15.5, 16.9, 18.5, 20.4),
    IMTReference(33, 12.2, 13.1, 14.2, 15.5, 16.9, 18.5, 20.3),
    IMTReference(34, 12.2, 13.1, 14.2, 15.4, 16.8, 18.5, 20.3),
    IMTReference(35, 12.1, 13.1, 14.2, 15.4, 16.8, 18.4, 20.3),
    IMTReference(36, 12.1, 13.1, 14.2, 15.4, 16.8, 18.4, 20.3),
    IMTReference(37, 12.1, 13.1, 14.1, 15.4, 16.8, 18.4, 20.3),
    IMTReference(38, 12.1, 13.0, 14.1, 15.4, 16.8, 18.4, 20.3),
    IMTReference(39, 12.0, 13.0, 14.1, 15.3, 16.8, 18.4, 20.3),
    IMTReference(40, 12.0, 13.0, 14.1, 15.3, 16.8, 18.4, 20.3),
    IMTReference(41, 12.0, 13.0, 14.1, 15.3, 16.8, 18.4, 20.4),
    IMTReference(42, 12.0, 12.9, 14.0, 15.3, 16.8, 18.4, 20.4),
    IMTReference(43, 11.9, 12.9, 14.0, 15.3, 16.8, 18.4, 20.4),
    IMTReference(44, 11.9, 12.9, 14.0, 15.3, 16.8, 18.5, 20.4),
    IMTReference(45, 11.9, 12.9, 14.0, 15.3, 16.8, 18.5, 20.5),
    IMTReference(46, 11.9, 12.9, 14.0, 15.3, 16.8, 18.5, 20.5),
    IMTReference(47, 11.8, 12.8, 14.0, 15.3, 16.8, 18.5, 20.5),
    IMTReference(48, 11.8, 12.8, 14.0, 15.3, 16.8, 18.5, 20.6),
    IMTReference(49, 11.8, 12.8, 13.9, 15.3, 16.8, 18.5, 20.6),
    IMTReference(50, 11.8, 12.8, 13.9, 15.3, 16.8, 18.6, 20.7),
    IMTReference(51, 11.8, 12.8, 13.9, 15.3, 16.8, 18.6, 20.7),
    IMTReference(52, 11.7, 12.8, 13.9, 15.2, 16.8, 18.6, 20.7),
    IMTReference(53, 11.7, 12.7, 13.9, 15.3, 16.8, 18.6, 20.8),
    IMTReference(54, 11.7, 12.7, 13.9, 15.3, 16.8, 18.7, 20.8),
    IMTReference(55, 11.7, 12.7, 13.9, 15.3, 16.8, 18.7, 20.9),
    IMTReference(56, 11.7, 12.7, 13.9, 15.3, 16.8, 18.7, 20.9),
    IMTReference(57, 11.7, 12.7, 13.9, 15.3, 16.9, 18.7, 21.0),
    IMTReference(58, 11.7, 12.7, 13.9, 15.3, 16.9, 18.8, 21.0),
    IMTReference(59, 11.6, 12.7, 13.9, 15.3, 16.9, 18.8, 21.0),
    IMTReference(60, 11.6, 12.7, 13.9, 15.3, 16.9, 18.8, 21.1)
    )

fun calculateIMT(berat: Double, tinggi: Double): Double {
    return berat / (tinggi /100 * tinggi / 100)
}

fun getIMTReference(gender: String, umurIMT: Int): IMTReference? {
    return when (gender.lowercase()) {
        "laki-laki" -> imtRefLaki.find { it.umur == umurIMT }
        "perempuan" -> imtRefPer.find { it.umur == umurIMT }
        else -> null
    }
}

fun calculateZScore(imt: Double, reference: IMTReference): Double {
    val nsbrAtas = reference.plus1SD - reference.median
    val nsbrBawah = reference.median - reference.minus1SD
    return if (imt > reference.median) {
        (imt - reference.median) / nsbrAtas
    } else {
        (imt - reference.median) / nsbrBawah
    }
}

fun getBMICategoryFromZScore(zScore: Double): String {
    return when {
        zScore < -3 -> "Gizi Buruk"
        zScore >= -3 && zScore <= -2 -> "Gizi Kurang"
        zScore >= -2 && zScore <= 1 -> "Gizi Baik"
        zScore in 1.0..2.0 -> "Beresiko gizi lebih"
        zScore in 2.0..3.0 -> "Gizi Lebih"
        else -> "Obesitas"

    }
}


