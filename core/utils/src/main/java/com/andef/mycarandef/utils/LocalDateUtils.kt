package com.andef.mycarandef.utils

import java.time.LocalDate

fun LocalDate.toInt() = year * 10000 + monthValue * 100 + dayOfMonth

fun Int.toLocalDate(): LocalDate {
    val year = this / 10000
    val month = (this % 10000) / 100
    val day = this % 100
    return LocalDate.of(year, month, day)
}