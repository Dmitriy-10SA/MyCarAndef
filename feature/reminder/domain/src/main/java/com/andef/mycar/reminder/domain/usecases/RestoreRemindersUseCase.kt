package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class RestoreRemindersUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke() = repository.restoreReminders()
}
