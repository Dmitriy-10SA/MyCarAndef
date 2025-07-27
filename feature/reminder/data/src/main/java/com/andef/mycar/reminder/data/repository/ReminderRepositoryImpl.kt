package com.andef.mycar.reminder.data.repository

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.content.ContextCompat
import com.andef.mycar.reminder.data.dao.ReminderDao
import com.andef.mycar.reminder.data.mapper.ReminderMapper
import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycar.reminder.domain.repository.ReminderRepository
import com.andef.mycar.reminder.presentation.ReminderReceiver
import com.andef.mycarandef.utils.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val application: Application,
    private val reminderMapper: ReminderMapper
) : ReminderRepository {
    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun addNotification(id: Long, text: String, time: Long) {
        try {
            val intent = ReminderReceiver.newIntent(application, id.toInt(), text)
            val pendingIntent = PendingIntent.getBroadcast(
                application,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } finally {
        }
    }

    private fun cancelNotification(id: Long) {
        try {
            val intent = ReminderReceiver.newIntent(application, id.toInt(), "")
            val pendingIntent = PendingIntent.getBroadcast(
                application,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            val notificationManager = ContextCompat.getSystemService(
                application,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.cancel(id.toInt())
        } finally {
        }
    }

    private fun changeNotification(id: Long, text: String, time: Long) {
        cancelNotification(id)
        addNotification(id, text, time)
    }

    override suspend fun getAllRemindersAsList(): List<Reminder> {
        return reminderDao.getAllRemindersAsList().map {
            reminderDbo -> reminderMapper.map(reminderDbo)
        }
    }

    override suspend fun addReminder(reminder: Reminder): Long {
        val id = reminderDao.addReminder(reminderMapper.map(reminder))
        addNotification(
            id = id,
            text = "${reminder.text}\nМашина: ${reminder.carName}",
            time = toEpochMillis(reminder.date, reminder.time)
        )
        return id
    }

    override suspend fun changeReminder(id: Long, text: String, date: LocalDate, time: LocalTime) {
        reminderDao.changeReminder(id, text, date.toInt(), time.toInt())
        val reminder = getReminder(id)
        changeNotification(
            id = id,
            text = "${text}\nМашина: ${reminder.carName}",
            time = toEpochMillis(date, time)
        )
    }

    override suspend fun getReminder(id: Long): Reminder {
        return reminderMapper.map(reminderDao.getReminder(id))
    }

    override suspend fun removeReminder(id: Long) {
        reminderDao.removeReminder(id)
        cancelNotification(id)
    }

    override fun getRemindersByCarId(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Reminder>> {
        return reminderDao.getRemindersByCarId(
            carId,
            startDate.toInt(),
            endDate.toInt()
        ).map { remindersDbo ->
            remindersDbo.map { reminderDbo ->
                reminderMapper.map(reminderDbo)
            }
        }
    }

    private fun toEpochMillis(date: LocalDate, time: LocalTime): Long {
        return date.atTime(time)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}