package com.andef.mycarandef.design.topbar.type

import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import java.time.LocalDate

sealed class UiTopBarType {
    data object Center : UiTopBarType()
    data object NotCenter : UiTopBarType()
    data class WithTabs(
        val tabs: List<UiTopBarTab>,
        val selectedTabIndex: Int,
        val onTabClick: (UiTopBarTab) -> Unit
    ) : UiTopBarType()
    data class WithCalendar(
        val weekCalendarState: WeekCalendarState,
        val currentDay: LocalDate,
        val onDayClick: (LocalDate) -> Unit,
        val withEvent: (LocalDate) -> Boolean
    ) : UiTopBarType()
}

open class UiTopBarTab(val id: Int, val title: String)