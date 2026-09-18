package com.andef.mycarandef.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

fun currentDateRangeForTab(tabId: Int, now: LocalDate): Pair<LocalDate, LocalDate> =
    when (tabId) {
        0 -> now to now
        1 -> {
            val startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            startOfWeek to startOfWeek.plusDays(6)
        }

        2 -> selectedMonthRange(now.year, now.monthValue)
        3 -> selectedYearRange(now.year)
        else -> now to now
    }

fun selectedMonthRange(year: Int, month: Int): Pair<LocalDate, LocalDate> {
    val selectedMonth = YearMonth.of(year, month)
    return selectedMonth.atDay(1) to selectedMonth.atEndOfMonth()
}

fun selectedYearRange(year: Int): Pair<LocalDate, LocalDate> =
    LocalDate.of(year, 1, 1) to LocalDate.of(year, 12, 31)

fun formatLocalDateRange(startDate: LocalDate, endDate: LocalDate): String {
    val today = LocalDate.now()
    val startOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfCurrentWeek = startOfCurrentWeek.plusDays(6)

    return when {
        startDate == endDate -> formatLocalDate(startDate)
        startDate == startOfCurrentWeek && endDate == endOfCurrentWeek -> "Текущая неделя"
        isFullMonthRange(startDate, endDate) -> "${getMonthName(startDate.monthValue)} ${startDate.year}"
        isFullYearRange(startDate, endDate) -> startDate.year.toString()
        else -> "${formatLocalDateForRange(startDate)} - ${formatLocalDateForRange(endDate)}"
    }
}

private fun formatLocalDateForRange(date: LocalDate): String =
    date.dayOfMonth.toString().padStart(2, '0') + "." +
            date.monthValue.toString().padStart(2, '0') + "." + date.year

private fun isFullMonthRange(startDate: LocalDate, endDate: LocalDate): Boolean {
    val month = YearMonth.from(startDate)
    return startDate == month.atDay(1) && endDate == month.atEndOfMonth()
}

private fun isFullYearRange(startDate: LocalDate, endDate: LocalDate): Boolean =
    startDate == LocalDate.of(startDate.year, 1, 1) &&
            endDate == LocalDate.of(startDate.year, 12, 31)

private fun getMonthName(month: Int): String = when (month) {
    1 -> "Январь"
    2 -> "Февраль"
    3 -> "Март"
    4 -> "Апрель"
    5 -> "Май"
    6 -> "Июнь"
    7 -> "Июль"
    8 -> "Август"
    9 -> "Сентябрь"
    10 -> "Октябрь"
    11 -> "Ноябрь"
    12 -> "Декабрь"
    else -> error("Invalid month: $month")
}
