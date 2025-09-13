package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetRemindersByCarIdUseCase @Inject constructor(private val repository: ReminderRepository) {
    operator fun invoke(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Reminder>> {
        return repository.getRemindersByCarId(carId, startDate, endDate)
    }
}