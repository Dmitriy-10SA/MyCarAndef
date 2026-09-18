package com.andef.mycarandef.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateRangeUtilsTest {
    @Test
    fun weekTabUsesMondayThroughSunday() {
        val range = currentDateRangeForTab(1, LocalDate.of(2026, 11, 18))
        assertEquals(LocalDate.of(2026, 11, 16), range.first)
        assertEquals(LocalDate.of(2026, 11, 22), range.second)
    }

    @Test
    fun monthTabUsesWholeCalendarMonth() {
        val range = currentDateRangeForTab(2, LocalDate.of(2026, 11, 18))
        assertEquals(LocalDate.of(2026, 11, 1), range.first)
        assertEquals(LocalDate.of(2026, 11, 30), range.second)
    }

    @Test
    fun yearTabUsesWholeCalendarYear() {
        val range = currentDateRangeForTab(3, LocalDate.of(2026, 11, 18))
        assertEquals(LocalDate.of(2026, 1, 1), range.first)
        assertEquals(LocalDate.of(2026, 12, 31), range.second)
    }

    @Test
    fun wholeMonthHasReadableTitle() {
        assertEquals(
            "Ноябрь 2026",
            formatLocalDateRange(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30))
        )
    }

    @Test
    fun wholeYearHasYearTitle() {
        assertEquals(
            "2026",
            formatLocalDateRange(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31))
        )
    }
}
