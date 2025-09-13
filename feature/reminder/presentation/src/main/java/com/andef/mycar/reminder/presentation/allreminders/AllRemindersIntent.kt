package com.andef.mycar.reminder.presentation.allreminders

import com.andef.mycarandef.car.domain.entities.Car
import java.time.LocalDate
import java.time.LocalTime

sealed class AllRemindersIntent {
    data class SubscribeToReminders(val carId: Long) : AllRemindersIntent()
    data class CurrentCarChoose(val car: Car) : AllRemindersIntent()
    data class DateSelected(val date: LocalDate) : AllRemindersIntent()
    data class DeleteDialogVisibleChange(val isVisible: Boolean) : AllRemindersIntent()
    data class DeleteReminder(val id: Long, val onError: (String) -> Unit) : AllRemindersIntent()
    data class ReminderBottomSheetVisibleChange(
        val isVisible: Boolean,
        val reminderId: Long? = null,
        val reminderText: String? = null,
        val reminderDate: LocalDate? = null,
        val reminderTime: LocalTime? = null
    ) : AllRemindersIntent()
}