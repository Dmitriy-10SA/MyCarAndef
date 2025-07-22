package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.repository.ReminderRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ChangeReminderUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke(
        id: Long,
        text: String,
        date: LocalDate,
        time: LocalTime,
        carId: Long,
        carName: String,
        carImageUri: String?
    ) = repository.changeReminder(id, text, date, time, carId, carName, carImageUri)
}