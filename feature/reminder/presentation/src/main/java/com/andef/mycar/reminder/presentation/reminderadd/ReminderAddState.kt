package com.andef.mycar.reminder.presentation.reminderadd

import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import java.time.LocalDate
import java.time.LocalTime

data class ReminderAddState(
    val reminderId: Long? = null,
    val reminderText: String = "",
    val reminderDate: LocalDate? = null,
    val reminderTime: LocalTime? = null,
    val repeatType: ReminderRepeatType? = null,
    val isLoading: Boolean = false,
    val datePickerVisible: Boolean = false,
    val timePickerVisible: Boolean = false,
    val saveButtonEnabled: Boolean = false,
    val isAdd: Boolean = true
)
