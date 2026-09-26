package com.andef.mycar.reminder.presentation.allreminders

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import java.time.LocalDate
import java.time.LocalTime

data class AllRemindersState(
    val reminders: List<Reminder> = listOf<Reminder>(),
    val remindersForScreenAsList: List<Reminder> = listOf<Reminder>(),
    val remindersLocalDatesForScreenAsSet: Set<LocalDate> = setOf<LocalDate>(),
    val reminderTextInBottomSheet: String? = null,
    val reminderIdInBottomSheet: Long? = null,
    val reminderDateInBottomSheet: LocalDate? = null,
    val reminderTimeInBottomSheet: LocalTime? = null,
    val reminderRepeatTypeInBottomSheet: ReminderRepeatType? = null,
    val reminderSheetVisible: Boolean = false,
    val calendarVisible: Boolean = false,
    val currentDate: LocalDate = LocalDate.now(),
    val deleteDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
