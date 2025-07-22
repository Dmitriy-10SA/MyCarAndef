package com.andef.mycar.reminder.data.repository

import com.andef.mycar.reminder.data.dao.ReminderDao
import com.andef.mycar.reminder.data.mapper.ReminderMapper
import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.repository.ReminderRepository
import com.andef.mycarandef.utils.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderMapper: ReminderMapper
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder): Long {
        return reminderDao.addReminder(reminderMapper.map(reminder))
    }

    override suspend fun changeReminder(
        id: Long,
        text: String,
        date: LocalDate,
        time: LocalTime,
        carId: Long
    ) {
        reminderDao.changeReminder(
            id,
            text,
            date.toInt(),
            time.toInt(),
            carId
        )
    }

    override suspend fun getReminder(id: Long): Reminder {
        return reminderMapper.map(reminderDao.getReminder(id))
    }

    override suspend fun removeReminder(id: Long) {
        reminderDao.removeReminder(id)
    }

    override fun getRemindersByCarId(carId: Long): Flow<List<Reminder>> {
        return reminderDao.getRemindersByCarId(carId).map { remindersDbo ->
            remindersDbo.map { reminderDbo ->
                reminderMapper.map(reminderDbo)
            }
        }
    }
}