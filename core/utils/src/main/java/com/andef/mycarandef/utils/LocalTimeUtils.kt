package com.andef.mycarandef.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalTime.toInt() = this.toSecondOfDay()

fun Int.toLocalTime() = LocalTime.ofSecondOfDay(this.toLong())


fun formatLocalTimeToString(time: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale("ru", "RU"))
    return time.format(formatter)
}