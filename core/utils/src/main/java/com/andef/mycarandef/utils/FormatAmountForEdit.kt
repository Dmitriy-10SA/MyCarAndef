package com.andef.mycarandef.utils

import kotlin.math.abs

fun formatAmountForEdit(value: Long): String {
    val rubles = value / 100
    val kopecks = abs(value % 100).toString().padStart(2, '0')
    return "$rubles,$kopecks"
}
