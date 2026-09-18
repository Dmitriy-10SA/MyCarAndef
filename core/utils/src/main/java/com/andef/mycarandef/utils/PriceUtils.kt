package com.andef.mycarandef.utils

import kotlin.math.abs

fun formatPriceRuble(value: Long): String {
    val rubles = value / 100
    val kopecks = abs(value % 100).toString().padStart(2, '0')
    val groupedRubles = rubles.toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    return "$groupedRubles.$kopecks₽"
}
