package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) = repository.addReminder(reminder)
}