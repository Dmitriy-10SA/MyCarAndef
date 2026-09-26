package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.repository.ReminderRepository
import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ChangeReminderUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke(
        id: Long,
        text: String,
        date: LocalDate,
        time: LocalTime,
        repeatType: ReminderRepeatType?
    ) {
        repository.changeReminder(id, text, date, time, repeatType)
    }
}
