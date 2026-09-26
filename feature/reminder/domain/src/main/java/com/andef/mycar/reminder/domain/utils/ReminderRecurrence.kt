package com.andef.mycar.reminder.domain.utils

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import java.time.LocalDate
import java.time.YearMonth

fun Reminder.occurrencesBetween(startDate: LocalDate, endDate: LocalDate): List<Reminder> {
    if (date > endDate || startDate > endDate) return emptyList()
    if (repeatType == null) return if (date >= startDate) listOf(this) else emptyList()

    val occurrences = mutableListOf<Reminder>()
    var occurrenceIndex = 0
    var occurrenceDate = occurrenceDateAt(date, repeatType, occurrenceIndex)
    while (occurrenceDate <= endDate) {
        if (occurrenceDate >= startDate) occurrences += copy(date = occurrenceDate)
        occurrenceIndex++
        occurrenceDate = occurrenceDateAt(date, repeatType, occurrenceIndex)
    }
    return occurrences
}

fun nextOccurrenceDate(
    date: LocalDate,
    repeatType: ReminderRepeatType?,
    fromDate: LocalDate
): LocalDate? {
    if (repeatType == null) return date.takeIf { it >= fromDate }

    var occurrenceIndex = 0
    var occurrenceDate = occurrenceDateAt(date, repeatType, occurrenceIndex)
    while (occurrenceDate < fromDate) {
        occurrenceIndex++
        occurrenceDate = occurrenceDateAt(date, repeatType, occurrenceIndex)
    }
    return occurrenceDate
}

private fun occurrenceDateAt(
    startDate: LocalDate,
    repeatType: ReminderRepeatType,
    occurrenceIndex: Int
): LocalDate {
    if (occurrenceIndex == 0) return startDate

    return when (repeatType) {
        ReminderRepeatType.WEEKLY -> startDate.plusDays(occurrenceIndex * 7L)
        ReminderRepeatType.MONTHLY -> dateInShiftedMonth(startDate, occurrenceIndex.toLong())
        ReminderRepeatType.FIRST_DAY_OF_MONTH -> startDate.withDayOfMonth(1)
            .plusMonths(occurrenceIndex.toLong())

        ReminderRepeatType.LAST_DAY_OF_MONTH -> {
            val startMonthLastDay = YearMonth.from(startDate).atEndOfMonth()
            val monthOffset = if (startDate < startMonthLastDay) {
                occurrenceIndex - 1L
            } else {
                occurrenceIndex.toLong()
            }
            YearMonth.from(startDate).plusMonths(monthOffset).atEndOfMonth()
        }

        ReminderRepeatType.EVERY_THREE_MONTHS -> {
            dateInShiftedMonth(startDate, occurrenceIndex * 3L)
        }

        ReminderRepeatType.YEARLY -> dateInShiftedYear(startDate, occurrenceIndex.toLong())
    }
}

private fun dateInShiftedMonth(startDate: LocalDate, months: Long): LocalDate {
    val month = YearMonth.from(startDate).plusMonths(months)
    return month.atDay(minOf(startDate.dayOfMonth, month.lengthOfMonth()))
}

private fun dateInShiftedYear(startDate: LocalDate, years: Long): LocalDate {
    val year = YearMonth.from(startDate).plusYears(years)
    return year.atDay(minOf(startDate.dayOfMonth, year.lengthOfMonth()))
}
