package com.andef.mycarandef.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toInt() = year * 10000 + monthValue * 100 + dayOfMonth

fun Int.toLocalDate(): LocalDate {
    val year = this / 10000
    val month = (this % 10000) / 100
    val day = this % 100
    return LocalDate.of(year, month, day)
}

fun formatLocalDate(date: LocalDate): String {
    if (date == LocalDate.now()) return "Сегодня"
    else if (date == LocalDate.now().minusDays(1)) return "Вчера"
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    return date.format(formatter)
}