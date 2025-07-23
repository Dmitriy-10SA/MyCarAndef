package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class RemoveReminderUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: Long) = repository.removeReminder(id)
}