package com.andef.mycar.reminder.presentation.reminderadd

import java.time.LocalDate
import java.time.LocalTime

sealed class ReminderAddIntent {
    data class ChangeReminderText(val text: String) : ReminderAddIntent()
    data class ChangeReminderDate(val date: LocalDate?) : ReminderAddIntent()
    data class ChangeReminderTime(val time: LocalTime?) : ReminderAddIntent()
    data class ChangeDatePickerVisible(val isVisible: Boolean) : ReminderAddIntent()
    data class ChangeTimePickerVisible(val isVisible: Boolean) : ReminderAddIntent()
    data class SaveClick(
        val carId: Long,
        val carName: String,
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    ) : ReminderAddIntent()

    data class InitReminderByLateReminder(
        val reminderId: Long,
        val onError: (String) -> Unit
    ) : ReminderAddIntent()
}