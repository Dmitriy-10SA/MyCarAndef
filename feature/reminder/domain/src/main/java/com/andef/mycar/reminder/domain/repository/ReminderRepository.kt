package com.andef.mycar.reminder.domain.repository

import com.andef.mycar.reminder.domain.entities.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface ReminderRepository {
    suspend fun getAllRemindersAsList(): List<Reminder>
    suspend fun addReminder(reminder: Reminder): Long
    suspend fun changeReminder(id: Long, text: String, date: LocalDate, time: LocalTime)
    suspend fun getReminder(id: Long): Reminder
    suspend fun removeReminder(id: Long)
    fun getRemindersByCarId(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Reminder>>
}