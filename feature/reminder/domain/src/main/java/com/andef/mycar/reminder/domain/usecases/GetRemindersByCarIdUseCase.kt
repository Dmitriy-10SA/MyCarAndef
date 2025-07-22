package com.andef.mycar.reminder.domain.usecases

import com.andef.mycar.reminder.domain.repository.ReminderRepository
import javax.inject.Inject

class GetRemindersByCarIdUseCase @Inject constructor(private val repository: ReminderRepository) {
    operator fun invoke(carId: Long) = repository.getRemindersByCarId(carId)
}