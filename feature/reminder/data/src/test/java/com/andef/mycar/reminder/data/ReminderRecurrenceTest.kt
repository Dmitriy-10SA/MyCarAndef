package com.andef.mycar.reminder.data

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import com.andef.mycar.reminder.domain.utils.occurrencesBetween
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ReminderRecurrenceTest {
    @Test
    fun weeklyReminderRepeatsOnSameWeekday() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.WEEKLY,
            startDate = LocalDate.of(2026, 9, 1),
            endDate = LocalDate.of(2026, 9, 30),
            expected = listOf(1, 8, 15, 22, 29).map { LocalDate.of(2026, 9, it) }
        )
    }

    @Test
    fun monthlyReminderKeepsAnchorDayAfterShortMonth() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.MONTHLY,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 3, 31),
            expected = listOf(
                LocalDate.of(2024, 1, 31),
                LocalDate.of(2024, 2, 29),
                LocalDate.of(2024, 3, 31)
            ),
            reminderDate = LocalDate.of(2024, 1, 31)
        )
    }

    @Test
    fun firstDayReminderKeepsInitialDateAndThenUsesFirstDay() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.FIRST_DAY_OF_MONTH,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 4, 30),
            expected = listOf(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 4, 1)
            ),
            reminderDate = LocalDate.of(2024, 1, 15)
        )
    }

    @Test
    fun firstDayReminderDoesNotDuplicateInitialFirstDay() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.FIRST_DAY_OF_MONTH,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 3, 31),
            expected = listOf(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 3, 1)
            )
        )
    }

    @Test
    fun lastDayReminderUsesActualLastDayOfEveryMonth() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.LAST_DAY_OF_MONTH,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 4, 30),
            expected = listOf(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 31),
                LocalDate.of(2024, 2, 29),
                LocalDate.of(2024, 3, 31),
                LocalDate.of(2024, 4, 30)
            ),
            reminderDate = LocalDate.of(2024, 1, 15)
        )
    }

    @Test
    fun lastDayReminderDoesNotDuplicateInitialMonthEnd() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.LAST_DAY_OF_MONTH,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 2, 29),
            expected = listOf(
                LocalDate.of(2024, 1, 31),
                LocalDate.of(2024, 2, 29)
            ),
            reminderDate = LocalDate.of(2024, 1, 31)
        )
    }

    @Test
    fun everyThreeMonthsKeepsAnchorDay() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.EVERY_THREE_MONTHS,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2024, 10, 31),
            expected = listOf(
                LocalDate.of(2024, 1, 31),
                LocalDate.of(2024, 4, 30),
                LocalDate.of(2024, 7, 31),
                LocalDate.of(2024, 10, 31)
            ),
            reminderDate = LocalDate.of(2024, 1, 31)
        )
    }

    @Test
    fun yearlyReminderRestoresLeapDay() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.YEARLY,
            startDate = LocalDate.of(2024, 1, 1),
            endDate = LocalDate.of(2028, 12, 31),
            expected = listOf(
                LocalDate.of(2024, 2, 29),
                LocalDate.of(2025, 2, 28),
                LocalDate.of(2026, 2, 28),
                LocalDate.of(2027, 2, 28),
                LocalDate.of(2028, 2, 29)
            ),
            reminderDate = LocalDate.of(2024, 2, 29)
        )
    }

    @Test
    fun recurringReminderDoesNotAppearBeforeItsStartDate() {
        assertOccurrenceDates(
            repeatType = ReminderRepeatType.MONTHLY,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 6, 30),
            expected = listOf(
                LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 6, 15)
            ),
            reminderDate = LocalDate.of(2026, 5, 15)
        )
    }

    @Test
    fun oneTimeReminderIsReturnedOnlyInsideRange() {
        val reminder = reminder(LocalDate.of(2026, 9, 26), null)

        assertEquals(
            listOf(reminder),
            reminder.occurrencesBetween(
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 30)
            )
        )
        assertEquals(
            emptyList<Reminder>(),
            reminder.occurrencesBetween(
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31)
            )
        )
    }

    private fun assertOccurrenceDates(
        repeatType: ReminderRepeatType,
        startDate: LocalDate,
        endDate: LocalDate,
        expected: List<LocalDate>,
        reminderDate: LocalDate = startDate
    ) {
        val occurrences = reminder(reminderDate, repeatType)
            .occurrencesBetween(startDate, endDate)
            .map { it.date }
        assertEquals(expected, occurrences)
    }

    private fun reminder(date: LocalDate, repeatType: ReminderRepeatType?) = Reminder(
        id = 1,
        text = "Заменить масло",
        date = date,
        time = LocalTime.of(18, 0),
        carId = 1,
        carName = "Моя машина",
        repeatType = repeatType
    )
}
